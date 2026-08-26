function slide(element,index) {
    console.log('slide param', index);
    let width=document.querySelector('.slide').offsetWidth;
    document.querySelector('.slide').style.transform = 'translateX(-' + (width * index) + 'px)';
    document.querySelectorAll('.dots span').forEach(el=>{
        el.classList.remove('active');
    });
    element.classList.add('active');
}