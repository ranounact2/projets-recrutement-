import asyncio
from playwright.async_api import async_playwright
from urllib.parse import urljoin, urlparse

BASE_URL = "http://localhost:8080"
VISITED = set()
TO_VISIT = {BASE_URL}
ERRORS = []

async def crawl():
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        context = await browser.new_context()
        page = await context.new_page()

        # Listen for console errors
        page.on("console", lambda msg: ERRORS.append(f"Console error on {page.url}: {msg.text}") if msg.type == "error" else None)
        
        # Listen for uncaught exceptions
        page.on("pageerror", lambda exc: ERRORS.append(f"Page error on {page.url}: {exc.message}"))

        while TO_VISIT:
            current_url = TO_VISIT.pop()
            if current_url in VISITED:
                continue
            
            VISITED.add(current_url)
            print(f"Visiting: {current_url}")
            
            try:
                response = await page.goto(current_url, wait_until="networkidle", timeout=15000)
                
                # Check for HTTP errors
                if response and not response.ok:
                    ERRORS.append(f"HTTP Error {response.status} on {current_url}")
                    continue
                
                # Check if page body contains stack traces (rudimentary check)
                content = await page.content()
                if "Whitelabel Error Page" in content or "java.lang." in content or "Exception" in content:
                    ERRORS.append(f"Possible stack trace or error page found on {current_url}")

                # Find all links
                links = await page.query_selector_all("a")
                for link in links:
                    href = await link.get_attribute("href")
                    if href:
                        full_url = urljoin(current_url, href)
                        # Remove fragment
                        full_url = full_url.split('#')[0]
                        parsed_url = urlparse(full_url)
                        
                        # Only follow links on the same host and port
                        if parsed_url.netloc == "localhost:8080" and full_url not in VISITED:
                            # Avoid crawling static assets or logout which might disrupt session if we add one later
                            if not any(full_url.endswith(ext) for ext in [".png", ".jpg", ".css", ".js", ".pdf", ".ico"]):
                                TO_VISIT.add(full_url)

            except Exception as e:
                ERRORS.append(f"Failed to visit {current_url}: {str(e)}")

        await browser.close()

    print("\n--- Crawl Results ---")
    print(f"Total pages visited: {len(VISITED)}")
    if not ERRORS:
        print("No errors found!")
    else:
        print(f"Found {len(ERRORS)} errors:")
        for err in set(ERRORS):
            print(f"- {err}")

if __name__ == "__main__":
    asyncio.run(crawl())
