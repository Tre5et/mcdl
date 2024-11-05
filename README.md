# MC Data Loader

![Maven Central Version](https://img.shields.io/maven-central/v/dev.treset.mcdl/mcdl?style=flat-square&link=https%3A%2F%2Fcentral.sonatype.com%2Fartifact%2Fdev.treset.mcdl%2Fmcdl
)

A Java-library that allows easy integration with most Minecraft related data.

## Installation
The library is split into multiple modules, each containing a different set of features.
Import the base module as well as the modules you require using your preferred build system:

<details>
<summary>Maven</summary>

```xml
<dependencies>
    <dependency>
        <groupId>dev.treset.mcdl</groupId>
        <artifactId>mcdl</artifactId>
        <version>{version}</version>
    </dependency>
    <dependency>
      <groupId>dev.treset.mcdl</groupId>
      <artifactId>mcdl-{module-name}</artifactId>
      <version>{version}</version>
    </dependency>
</dependencies>
```
</details>

<details>
<summary>Gradle</summary>

```groovy
dependencies {
    implementation 'dev.treset.mcdl:mcdl:{version}'
    implementation 'dev.treset.mcdl:mcdl-{module-name}:{version}'
}
```

> [!TIP]
> If you are importing multiple modules, you can declare them programmatically:
> ```groovy
> def mcdlVersion = '{version}'
> def mcdlModules = ['{module-name1}', '{module-name2}']
> dependencies {
>     implementation 'dev.treset.mcdl:mcdl:$mcdlVersion'
>     for(module in mcdlModules) {
>         implementation 'dev.treset.mcdl:mcdl-$module:$mcdlVersion'
>     }
> }
> ```
</details>

<details>
<summary>Gradle Kotlin DSL</summary>

```kotlin
dependencies {
    implementation("dev.treset.mcdl:mcdl:{version}")
    implementation("dev.treset.mcdl:mcdl-{module-name}:{version}")
}
```

> [!TIP]
> If you are importing multiple modules, you can declare them programmatically:
> ```kotlin
> val mcdlVersion = "{version}"
> val mcdlModules = arrayOf("{module-name1}", "{module-name2}")
> dependencies {
>     implementation("dev.treset.mcdl:mcdl:$mcdlVersion")
>     for(module in mcdlModules) {
>         implementation("dev.treset.mcdl:mcdl-$module:$mcdlVersion")
>     }
> }
> ```
</details>

## Modules

<details><summary>Assets</summary>

- Fetching of Minecraft asset indexes
- Fetching and downloading of Minecraft assets
- Resolving assets to virtual assets
</details>

<details><summary>Auth</summary>

- Authenticating a Minecraft user using a Microsoft account
- Getting the users profile data
- Storing and reusing login tokens
</details>

<details><summary>Fabric</summary>

- Fetching Fabric verions
- Fetching Fabric loader profiles
- Downloading the Fabric client
- Downloading Fabric libraries
</details>

<details><summary>Forge</summary>

- Fetching Forge versions
- Downloading and installing the Forge client and libraries
</details>

<details><summary>Java</summary>

- Fetching Minecraft Java runtimes
- Downloading Minecraft Java files
</details>

<details><summary>Minecraft</summary>

- Fetching Minecraft versions
- Fetching Minecraft version details
- Downloading the Minecraft client
- Downloading Minecraft libraries
</details>

<details><summary>Mods</summary>

- Searching for Mods on Modrinth or CurseForge or both platforms combined
- Getting specific Mods from Modrinth or CurseForge
- Getting Mod Versions from Modrinth or CurseForge or both platforms combined
- Downloading Mod Versions
</details>

<details><summary>Mojang</summary>

- Fetching Minecraft Profiles for UUIDs
- Fetching Minecraft Users for usernames
</details>

<details><summary>NeoForge</summary>

- Fetching NeoForge versions
- Downloading and installing the NeoForge client and libraries
</details>

<details><summary>QuiltMC</summary>

- Fetching QuiltMC versions
- Fetching QuiltMC loader profiles
- Downloading the QuiltMC client
- Downloading QuiltMC libraries
</details>

<details><summary>Resourcepacks</summary>

- Parsing resourcepack files or directories
</details>

<details><summary>Saves</summary>

- Parsing Minecraft world directories to get world name, description and icon
- Parsing Minecraft Server files to get server data
</details>

## Usage

Each module has a Data Loader class, named `{Module}DL`, which contains static functions for most supported actions.
Most of these function are also accessible as methods on their respective objects.

Specifics about each functions usage can be found in the JavaDoc.

### Setting a User Agent
A global user agent can be set using `HttpUtil.setUserAgent(String userAgent)`.
This is highly recommended to do at startup to uniquely identify your application.
Otherwise, the default `mcdl/{version}` will be used.

A user agent should ideally follow the format `{applicaion-id}/{version}`.

### Initializing the auth module
To authenticate a user you need to create a Microsoft Identity Platform application and set the client id using `AuthDL.setClientId(String clientId)`.

To create the application you can follow this somewhat decent [tutorial by Microsoft](https://learn.microsoft.com/en-us/entra/identity-platform/quickstart-register-app?tabs=certificate). After creating the application, ensure the following settings:
- In `Authentication`, the platform `Mobile and desktop applications` is added and configured with the default redirect URI `http://localhost:9095` or another redirect URI, as configured using `AuthDL.setRedirectUri(String redirectUri)`.
- In `Authentication`, `Supported Account types` is set to `Personal Microsoft accounts only`.
- In `API permissions`, the permission `Micorsoft Graph`: `User.Read` is configured.

To allow Minecraft authentication, you then need to apply for you application to be approved by Mojang using [this form](https://forms.office.com/Pages/ResponsePage.aspx?id=v4j5cvGGr0GRqy180BHbR-ajEQ1td1ROpz00KtS8Gd5UNVpPTkVLNFVROVQxNkdRMEtXVjNQQjdXVC4u). This can unfortunately take multiple weeks.

### Initializing the mods module
To fetch mods from modrinth and curseforge you need to initialize API authentication for both services:

- For Modrinth a User Agent needs to be set. This can be done using `ModsDL.setModrinthUserAgent(String userAgent)`. This user agent must uniquely identify your application. It is not acceptable to copy the default user agent or use any other user agent that only identifies MCDL.
- For CurseForge you need to set a valid API key using `ModsDL.setCurseForgeApiKey(String key)`. You can obtain a key for your application using the [CurseForge developer console](https://console.curseforge.com/?#/api-keys).

### Data caching
Caching can be set for different types of requests. There are three types of caching:
- `NoCaching`: No data is ever cached, each request is sent to the target server
- `MemoryChaching`: Data is cached for the runtime of the application, but not saved to disk
- `PersistentCaching`: Data is cached for the runtime of the application and saved to disk

Data will be cached for as long as the data header specifies, or for a default duration, which can be set using `setCacheDuration(long durationMs)` on the `Caching` object. Default is 10 minutes.

#### Setting global caching

The global caching strategy for all requests can be set using ``HttpUtil.setDefaultCaching(Caching.{STRATEGY}> caching)``. Default is `RuntimeCaching`.

#### Setting module specific caching

Most modules allow you to set a caching strategy for the module, which overrides the global caching strategy. This can be set using `{Module]DL.setCaching(Caching.{STRATEGY} caching)`. If it is set to `null` (default) the global caching strategy will be used.

## Issues And Contributions

Issues can be reported on the [GitHub Issue Tracker](https://github.com/Tre5et/mcdl/issues). Contributions are welcome and can be made by creating a pull request.

## License

This library is available under the GNU LGPLv3 license. Feel free to redistribute, modify and incorporate it in your own open or partially closed source projects.

## Support Me

Support development on [Ko-fi](https://ko-fi.com/treset).



