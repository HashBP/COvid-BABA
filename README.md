# COvid-BABA
> Your Covid First Aid

### Problem Statement:
As of current Covid situation, There is a lot of variation in number of cases in different parts of our country. At the time of travel we may urge to know about the current Live Covid situation in the place we are in. But there is lot of info on internet and we may keep wandering in the cloud info and still return empty handed. So, We need a place where we can get all about Covid related information at one place. 

### Proposed Solution
This app give user an interface to overcome all the problems regarding information aboutCoivid situation in our surrounding and to keep track of the current Covid cases in your area and country and also to provides  an interface where you can get all the useful links at one place regarding Covid. Its features include count of live Covid cases and links redirecting you to some very useful websites. 

### Functionality & Concepts used :
Following are few android concepts used to achieve the functionalities in app :
* Combination of different Layouts : Most of the activities in the app uses a flexible layout, which is easy to handle for different screen sizes.
* Simple & Easy Views Design : Use of familiar audience EditText with hints and interactive buttons made it easier for anyone to register without providing any detailed instructions pages.
* This application uses GPS tracking techniques by acquiring the geo-location from the driver's mobile device and mapping it to the database. Google API’s are used for accuracy in finding the current situation of your state.
* Also there is an Image slider in the login page which keeps on sliding posters related to Covid precaution.
* There is a color changing function in the background which tells you the zone you are in (i.e Red, Orange or Green)  and changed background color of the text view according to the number of cases detected in your state. 
![This is an image](https://drive.google.com/file/d/142DXy_I-OLOdYgVqCMs_U20FUyDEs3PK/view?usp=sharing)
* Few animations are used in the app while showing the stats.
* Use of OKHTTP for fetching API for covid related numbers of states and India.
## Application Link & Future Scope :

* You can access the app :<br/> 
APK file :-  https://drive.google.com/file/d/1u8SAllJzjor-N_d7XypjCbaJUeZ6nMsV/view?usp=sharing <br/> 
Source code :- https://github.com/HashBP/COvid-BABA

There are few improvements can be made in this project : <br/>
* Adding custom search window, So someone can get details about anyh place by searching the name of it.
* More precise data like including the district wise stats about Covid (didn't added because of unavailability of open API's).
* Adding some navigation fragments for better functioning of the app instid of using visibility property. 
