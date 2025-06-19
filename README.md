<h1 align="center">
    <picture>
      <img height="280" alt="The Angels App" src="https://i.imagesup.co/images2/6174487873902d7c583a9a71f78c3c7a99e5b6be.png">
    </picture>
  </a>
  <br>
  
  <a href="https://github.com/theAngelsAPP/TheAngels-MobileApp">
    <img src="https://img.shields.io/badge/Mobile%20App%20Development-6A5ACD?style=flat-square">
  </a>
  
  <a href="https://github.com/theAngelsAPP/TheAngels-MobileApp">
    <img src="https://img.shields.io/badge/Java-FF4500?style=flat-square&logo=java&logoColor=white">
  </a>
  <a href="https://github.com/theAngelsAPP/TheAngels-MobileApp">
    <img src="https://img.shields.io/github/repo-size/theAngelsAPP/TheAngels-MobileApp?color=20B2AA&style=flat-square">
  </a>
  <a href="https://github.com/theAngelsAPP/TheAngels-MobileApp/commits/main">
    <img src="https://img.shields.io/github/commit-activity/m/theAngelsAPP/TheAngels-MobileApp?color=DC143C&style=flat-square">
  </a>
  <a href="https://github.com/theAngelsAPP/TheAngels-MobileApp/commits/main">
    <img src="https://img.shields.io/github/last-commit/theAngelsAPP/TheAngels-MobileApp?color=FFA500&style=flat-square">
  </a>
  <a href="https://github.com/theAngelsAPP/TheAngels-MobileApp/issues">
    <img src="https://img.shields.io/github/issues/theAngelsAPP/TheAngels-MobileApp?color=32CD32&style=flat-square">
  </a>
  <a href="https://github.com/theAngelsAPP/TheAngels-MobileApp/issues?q=is%3Aissue+is%3Aclosed">
    <img src="https://img.shields.io/github/issues-closed/theAngelsAPP/TheAngels-MobileApp?color=1E90FF&style=flat-square">
  </a>
</h1>
<h2 align="center" style="color:#6A5ACD; font-style:italic;">Project By:</h2>
<div align="center" style="display:flex; justify-content:center; gap:20px;">
  <a href="https://www.linkedin.com/in/omergamliel/" target="_blank">
    <img src="https://i.imagesup.co/images2/5c1437683cdc548d425031598edf9b7cf7f1da22.png" style="width:100px; height:100px; border-radius:50%;" />
</a>
  <a href="https://www.linkedin.com/in/batel-gofleyzer-0a7a45206/" target="_blank">
    <img src="https://i.imagesup.co/images2/ac78807b55790ba5fb1d5675e15acc60db939932.png" style="width:100px; height:100px; border-radius:50%;" />
  </a>
</div>

## Local Configuration

The following files are intentionally excluded from version control:

- `secrets.properties`
- `app/google-services.json`
- `key/key.jks`
- `private_key.pepk`

Create a `secrets.properties` file or define the environment variables `GOOGLE_MAPS_API_KEY` and `GOOGLE_PLACES_API_KEY` before building the project:

```properties
GOOGLE_MAPS_API_KEY=your_google_maps_api_key
GOOGLE_PLACES_API_KEY=your_google_places_api_key
```

Download your Firebase `google-services.json` and place it inside the `app/` directory. Keep your signing keys (`key.jks` and `private_key.pepk`) outside of the repository and reference them via environment variables or a local `key.properties` file.

Previously exposed keys have been revoked. Make sure to generate new credentials for your environment.

