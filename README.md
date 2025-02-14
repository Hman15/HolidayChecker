**

## HolidayChecker

**
Check if the input date is a holday by using [Calendarific](https://calendarific.com/api-documentation),  [Abstract API](https://www.abstractapi.com/api/holidays-api) &  [HolidayAPI](Holiday%20API)

___________________________
**Installation**
1. Get [calendarcheckercore-release.aar](https://github.com/Hman15/HolidayChecker/releases/tag/1.0.0) AAR file from *Release* tab
2. Create a "libs" folder in your app module directory, and put calendarcheckercore-release.aar in there. Its path should look like this `{ProjectName}/app/libs/calendarcheckercore-release.aar`
3. Add necessary dependencies into your module level build.gradle (or build.gradle.kts):


> dependencies {    
> implementation("com.google.android.material:material:1.12.0")  
>     implementation(files("libs/calendarcheckercore-release.aar"))  
>     implementation("com.squareup.retrofit2:retrofit:2.11.0")  
>     implementation("com.squareup.retrofit2:converter-gson:2.11.0")

--------------------------------
**How-To-Use**
- Call `HolidayChecker.initialize(calendarificKey: String, abstractKey: String, holidayKey: String)` to initialize the SDK. Note that API key parameters are optional.

- Call `HolidayChecker.isHoliday(year: Int,  month: Int,  day: Int,  checkType: Int = CheckType.ANY.value, onSuccess: (Boolean) -> Unit,  onFailure: (List<BaseResponse.Error>) -> Unit)` to use the SDK. Result will be returned via `onSuccess: (Boolean) -> Unit`, while possible errors and exceptions will be returned via `onFailure: (List<BaseResponse.Error>) -> Unit`

*Parameter requirements:*

- year: Integer for a valid year between 0 and 3000.
- month: Integer for a valid month between 1 and 12.
- day: Integer for a valid day in a month between 1 and 31 (depending on which month).
- checkType: Interger
    - ANY (0) – if any of the APIs return that a given date is a holiday, return true .
    -  ALL (1) – if any of the APIs return that a given date is NOT a holiday, return false .
    -  CONSENSUS (2) – if the majority of the APIs return that a given date is a holiday, return true.

*Error codes:*

- Invalid inputs:
    - 100: Invalid year
    - 101: Invalid month
    - 102: Invalid day
    - 103: Invalid checkType
- API error codes: Please refer to each API's document for more detail
-------
**Example App**
Get the [sdk-test.apk](https://github.com/Hman15/HolidayChecker/releases/download/1.0.0/sdk-test.apk) file from [Release tab](https://github.com/Hman15/HolidayChecker/releases/tag/1.0.0)