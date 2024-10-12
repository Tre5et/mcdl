# MC Data Loader

![Maven Central Version](https://img.shields.io/maven-central/v/dev.treset.mcdl/mcdl?style=flat-square&link=https%3A%2F%2Fcentral.sonatype.com%2Fartifact%2Fdev.treset.mcdl%2Fmcdl
)

A Java-library that allows easy integration with most Minecraft related data.

## Installation
The library is split into multiple modules, each containing a different set of features.
Import the modules you require using your preferred build system:

<details>
<summary>Maven</summary>

```xml
<dependencies>
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
  implementation 'dev.treset.mcdl:mcdl-{module-name}:{version}'
}
```

> [!TIP]
> If you are importing multiple modules, you can declare them programmatically:
> ```groovy
> def mcdlVersion = '{version}'
> def mcdlModules = ['{module-name1}', '{module-name2}']
> dependencies {
>   for(module in mcdlModules) {
>     implementation 'dev.treset.mcdl:mcdl-$module:$mcdlVersion'
>   }
> }
> ```
</details>

<details>
<summary>Gradle Kotlin DSL</summary>

```kotlin
dependencies {
  implementation("dev.treset.mcdl:mcdl-{module-name}:{version}")
}
```

> [!TIP]
> If you are importing multiple modules, you can declare them programmatically:
> ```kotlin
> val mcdlVersion = "{version}"
> val mcdlModules = arrayOf("{module-name1}", "{module-name2}")
> dependencies {
>   for(module in mcdlModules) {
>     implementation("dev.treset.mcdl:mcdl-$module:$mcdlVersion")
>   }
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
- Fetching Forge installers
- Downloading and installing the Forge client (for Minecraft 1.5.2 and above)
- Downloading Forge libraries
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

### Data caching
Caching can be set for different types of requests. There are three types of caching:
- `NoCaching`: No data is ever cached, each request is sent to the target server
- `MemoryChaching`: Data is cached for the runtime of the application, but not saved to disk
- `PersistentCaching`: Data is cached for the runtime of the application and saved to disk

Data will be cached for as long as the data header specifies, or for a default duration, which can be set using `setCacheDuration(long durationMs)` on the `Caching` object. Default is 10 minutes.

#### Setting global caching

The global caching strategy for all requests can be set using ``HttpUtil.setDefaultCaching(Caching.{STRATEGY}> caching)``. Default is `RuntimeCaching`.

#### Setting module specific caching

Most modules allow you to set a caching strategy for the module, which overrides the global caching strategy. This can be set using `{Module]DL.setCaching(Caching.{STRATEGY}> caching)`. If it is set to `null` (default) the global caching strategy will be used.

## Issues And Contributions

Issues can be reported on the [GitHub Issue Tracker](https://github.com/Tre5et/mcdl/issues). Contributions are welcome and can be made by creating a pull request.

## License

This library is available under the GNU LGPLv3 license. Feel free to redistribute, modify and incorporate it in your own open or partially closed source projects.

## Support Me

Support development on [Ko-fi](https://ko-fi.com/treset).



