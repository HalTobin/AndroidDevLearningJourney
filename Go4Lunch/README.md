# Go4Lunch

Installation du projet Go4Lunch :
1. Générez un clef api Google (https://developers.google.com/maps/documentation/places/web-service/get-api-key)
2. Rendez-vous dans le fichier "gradle.properties" (situé à la racine du projet) et remplacez le contenu de la variable "MAPS_API_KEY" par votre clef api
3. Enregistrez l'application sur un compte "Facebook Developer" (https://developers.facebook.com/docs/facebook-login/android)
4. Lorque vous en serez à l'étape "4. Modifier vos ressources et votre manifeste", à la place de copier les valeurs fournies dans le fichier "/app/res/values/strings.xml", copiez les dans le fichier "gradle.properties" dans leurs champs respectifs
5. Le projet est prêt à être utilisé !