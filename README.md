# react-native-taplytics

A native js API for the native taplytics APIs.
Does it's best to be compatible with the [Taplytics JS API][1], with
some added APIs for `performBackgroundFetch` and push notifications
(not yet implemented).

[1]: https://taplytics.com/docs/javascript-sdk/reference

## iOS Installation

1. `npm install react-native-taplytics --save`
2. In XCode's project navigator, right click on your project and choose "Add
   files to <your project>..."
3. Choose `node_modules/react-native-taplytics/RNTaplytics.xcodeproj`
3. Repeat to add `node_modules/react-native-taplytics/taplytics-ios-sdk/Taplytics.framework`
4. Optionally drag these new libraries to your project's Libraries folder
5. In XCode's project navigator, click on your project, choose your app's build target,
   and go to Build Phases
6. Add libRNTaplytics.a, CoreTelephony.framework, and SystemConfiguration.framework to the 'Link Binary with Libraries' step. Then add Taplytics.framework to Embedded Binaries.
7. In your project target's Build Settings tab, find 'Framework Search Paths' and add two entries:
    * `$(inherited)`
    * `../node_modules/react-native-taplytics/taplytics-ios-sdk/Taplytics.framework`
8. In XCode's project navigator, find `RNTaplytics.xcodeproj` and set all the search paths
   other than `$(inherited)` to `recursive`

## Android Installation

1. `npm install react-native-taplytics --save`

2. In `android/build.gradle`, add `url "https://github.com/taplytics/Taplytics-Android-SDK/raw/master/AndroidStudio/"`
   to `allprojects { repositories { maven { } } }`:

   ```
   allprojects {
       repositories {
           mavenLocal()
           jcenter()
           maven {
               // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
               url "$projectDir/../../node_modules/react-native/android"
               url "https://github.com/taplytics/Taplytics-Android-SDK/raw/master/AndroidStudio/"
           }
       }
   }
   ```

3. In `android/settings.gradle`, add the following two lines:
   ```
   include ':RNTaplytics'
   project(':RNTaplytics').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-taplytics/android')
   ```

4. In `android/app/build.gradle`, add the following compilation step to `dependencies { }`:
   ```
   compile project(':RNTaplytics')
   ```

## API

See the [Taplytics JS API reference][1].
