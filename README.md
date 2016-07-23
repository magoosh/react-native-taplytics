# react-native-taplytics


## iOS Installation

1. `npm install react-native-taplytics --save`
2. In XCode's project navigator, right click on your project and choose "Add
   files to <your project>..."
3. Choose `node_modules/react-native-taplytics/RNTaplytics.xcodeproj`
3. Repeat to add `node_modules/react-native-taplytics/taplytics-ios-sdk/Taplytics.framework`
4. Optionally drag these new libraries to your project's Libraries folder
5. In XCode's project navigator, click on your project, choose your app's build target,
   and go to Build Phases
6. Add libRNTaplytics.a, Taplytics.framework, CoreTelephony.framework, and SystemConfiguration.framework to the 'Link Binary with Libraries' step
7. In your project target's Build Settings tab, find 'Framework Search Paths' and add two entries:
    * `$(inherited)`
    * `../node_modules/react-native-taplytics/taplytics-ios-sdk/Taplytics.framework`
8. In XCode's project navigator, find `RNTaplytics.xcodeproj` and set all the search paths
   other than `$(inherited)` to `recursive`

## Android Installation
