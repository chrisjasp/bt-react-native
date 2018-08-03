
# bt-react-native

## Getting started

`$ npm install bt-react-native --save`

### Mostly automatic installation

`$ react-native link bt-react-native`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `bt-react-native` and add `RNBraintree.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNBraintree.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNBraintreePackage;` to the imports at the top of the file
  - Add `new RNBraintreePackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':bt-react-native'
  	project(':bt-react-native').projectDir = new File(rootProject.projectDir, 	'../node_modules/bt-react-native/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':bt-react-native')
  	```


## Usage
```javascript
import RNBraintree from 'bt-react-native';

// TODO: What to do with the module?
RNBraintree.addToken(string: <Gateway Token From Server>);

/* Result
	* {nonce: "", type: "visa", isDefault: true, description: "...card ending in 1111"}
*/
let btResult = RNBraintree.fetchNonce({
	name: "Home Simpson",
	number: "4111111111111111",
	expMonth: "12",
	expYear: "2020",
	cvc: "123"
});
```
  