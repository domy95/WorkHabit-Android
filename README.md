# Workhabit

![Workhabit logo](https://github.com/udAL/WorkHabit-Android/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

[![Google Play](https://www.mobicastle.com/wp-content/uploads/2018/10/google-play-badge-2.png.pagespeed.ce.JoKQUVQM5Y.png)](https://play.google.com/store/apps/details?id=cat.coronout.workhabit)

Application with tips for more healthy teleworking

WorkHabit is an application that provides tips for people who do Telework to have a better experience and not lose healthy habits. Pauses to rest, pauses to stretch, ... these will be the messages that the application will provide you smartly, limiting your working hours. Enjoy Telework with WorkHabit.

## Table of contents
* [Features](#features)
* [Setup](#setup)
* [Contributors](#contributors)
* [Acknowledgments](#acknowledgments)
* [License](#license)

---

## Features
- [Android AlarmManager](https://developer.android.com/reference/android/app/AlarmManager)
- [Android Notifications](https://developer.android.com/guide/topics/ui/notifiers/notifications)
- [Google Gson](https://github.com/google/gson)
- [Google Firebase](https://github.com/firebase/firebase-android-sdk)
- [Google MaterialDesign](https://github.com/material-components/material-components-android/)

---

## Setup
Clone this repo to your desktop and use AndroidStudio importing it as gradle project. Use AndroidStudio
to run and debug the app.

This app was distributed using a *Coronout private signature*. If you want to do modifications and 
distribute it you must change application id, and, please, change app name too.

### Gradle configuration
**Compile SDK version:** 29
**Build Tools version:** 29.0.3
**Minimum SDK version:** 23
**Target SDK version:** 29

### AndroidManifest configuration

**Launcher:** MainActivity

**Main theme:** AppTheme

**Allow backup:** True

**Supports RTL:** True

**Receivers:**
- WorkhabitPublisher
- RebootReceiver

**Permissions:**
- RECEIVE_BOOT_COMPLETED
- VIBRATE

---

## Contributors

**David Passola** - *(Coronout team)*
- [Github](https://github.com/dpassola)
- [LinkedIn](https://es.linkedin.com/in/davidpassola)

**Eudald Rossell** - *(Coronout team)*
- [Github](https://github.com/udAL)
- [LinkedIn](https://es.linkedin.com/in/eudaldrossell)

**Artur Domínguez** - *(Coronout team)*
- [Github](https://github.com/domy95)
- [LinkedIn](https://es.linkedin.com/in/artur-dominguez-554846b9)

---

## Acknowledgments

Thanks to all the [Hackovid](https://hackovid.cat/) contributors for the hard work of these two weeks in creating this incredible event and also thanks to all the developers, over 400, for making this possible!

---

## License
> You can check out the full license [here](https://github.com/udAL/WorkHabit-Android/blob/master/LICENSE.md)

This project is licensed under the terms of the **Apache 2.0** license.

    Copyright 2020 Coronout
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
      http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
