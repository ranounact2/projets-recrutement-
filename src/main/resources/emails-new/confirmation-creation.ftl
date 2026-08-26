<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
    <div style="background-color: #f4f4f4; padding: 20px; border-radius: 5px; margin-bottom: 20px;">
        <h2 style="color: #2c3e50; margin-top: 0;">Confirmation de création d'annonce</h2>
    </div>
    
    <div style="padding: 20px; background-color: #ffffff; border: 1px solid #ddd; border-radius: 5px;">
        <p><strong>Bonjour,</strong></p>
        
        <p>Votre annonce a été ajoutée avec succès. Merci de patienter la validation de votre annonce.</p>
        
        <#if job?? && job.title??>
        <p><strong>Titre de l'annonce :</strong> ${job.title}</p>
        </#if>
        
        <#if job?? && job.secretCode??>
        <p>Vous pouvez consulter et gérer vos annonces en utilisant le lien suivant :</p>
        <p style="margin: 20px 0;">
            <a href="${host}/m-office/mes-annonces/${job.secretCode}" 
               style="display: inline-block; padding: 10px 20px; background-color: #3498db; color: #ffffff; text-decoration: none; border-radius: 5px;">
                Accéder à mes annonces
            </a>
        </p>
        </#if>
        
        <p style="margin-top: 30px;">Cordialement,<br>L'équipe Emplois Maroc</p>
    </div>
    
    <div style="margin-top: 20px; padding: 15px; background-color: #ecf0f1; border-radius: 5px; font-size: 12px; color: #7f8c8d;">
        <p style="margin: 0;">Cet email a été envoyé automatiquement, merci de ne pas y répondre.</p>
    </div>
</body>
</html>
