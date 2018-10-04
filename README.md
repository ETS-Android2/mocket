<img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/start_logo.png?token=AYaVqLkgrwW69b5EpPfjC_AnZAb-QsX7ks5bvr2twA%3D%3D" height="140" alt="mocket"/>

Mocket is an android application helping users to memorize words.

Unique Features
---
-	Unique testing system helping users to memorize words in lifetime.
-	Supports built in dictionary. Users can lookup words and directly add to memory list.
-	Keeps bothering users to finish daily test. (A duration can be configurable).
-	Supports a fun card game(words get picked randomly form memory list).
-	Shows a graph that contains statics of memory, game, and test.


Logic of Lifetime Memory
---

According to [Ebbinghaus's forget curve](https://en.wikipedia.org/wiki/Forgetting_curve), people get a stronger memory when they are able to recall it longer period of time.

So, Mocket gives tests in certain periods.
<img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/memory_duration.png?token=AYaVqFo_xMe7a8gAXsGhN3tjKNhkY27eks5bvs4dwA%3D%3D" />

As shown on above picture, it gives tests everyday for first three days. After that, the period gets longer, and this helps users to memorize words for lifetime.

Third Party Libraries Used
---
 - [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) in graph feature.
 - [Glide](https://github.com/bumptech/glide) to optimize images.
 - [EasyFlipView](https://github.com/wajahatkarim3/EasyFlipView) for card flipping animation.


Table of Layout Contents
------------------------
   * [Login](#login-feature)
   * [Memory](#memory-feature)
   * [Daily Test & Game](#daily-test-and-game-feature)
   * [Graph](#graph-feature)
   * [Profile](#profile-feature)


Login Feature
---

<img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_logo.png?token=AYaVqHy4UpoUMt7fpUrskDGwNf_szqASks5bvtBFwA%3D%3D" height="350" />   <img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_login.png?token=AYaVqEGC43D4K2moUhLT2FZonRqYEzv-ks5bvtBgwA%3D%3D" height="350" />   <img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_signUp.png?token=AYaVqJahbp4D_xhfGYZNI2ePUavEVl1Vks5bvtB9wA%3D%3D" height="350" />   <img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_forgotPassword.png?token=AYaVqOjeG9N_Xt5hcMDmJjwlOAKGq6Azks5bvtChwA%3D%3D" height="350" />

- Used [Firebase Realtime Database](https://firebase.google.com/docs/database/).
- It stores encrypted passwords.
- After signup, email verification should be done to login.
- When forgot password, users can send password reset link to their emails.

Memory Feature
---

<img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_memoryActivity.png?token=AYaVqOKIdIJW56uz4E8G8QLVFAdigYblks5bvtGzwA%3D%3D" height="350"/> <img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_lookup.png?token=AYaVqA9RC2_-7akZqHFFKAL_Kz0BIR87ks5bvtHJwA%3D%3D" height="350"/>

- When adding words, users can lookup on built in dictionary.
- Supports pronunciation feature.
- Today's memory list has all words added on that day.
- Users can have a quiz for today's memorized words.


Daily Test and Game Feature
---

This screen has two fun features, daily test and game.

<img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_gameActivity.png?token=AYaVqOGUXpiKjhUlxQlp64jemN5EsyDbks5bvtJzwA%3D%3D" height="350"/>


### Daily Test

<img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_test.png?token=AYaVqFZ-GfMzcHK15SyuSCEPcZV7sy3Vks5bvtPQwA%3D%3D" height="350" /> <img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_testBack.png?token=AYaVqLRuISM1kpK6Lasp6LzhR_oie8aCks5bvtPdwA%3D%3D" height="350"/>

- For certain period mentioned earlier, all the words, which should be tested today, will be shown on the cards.
- After guessing a definition of the shown word, users flip the card.
  - Confused emoticon: keep it in the iteration.
  - Confidence emoticon: remove the word from the iteration.
- It iterates until no word left.


### Game

<img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_gameCorrect.png?token=AYaVqNtx9Lsd_zOyG8hRT8NommTim4Xnks5bvtJ-wA%3D%3D" height="350"/> <img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_gameIncorrect.png?token=AYaVqE6VcFi1lHxHRgPBZCmRtR0yimQFks5bvtO0wA%3D%3D" height="350"/>

- For each word, 4 random definitions will appear.
- Users choose a definition from the 4 options.
- Correct and incorrect count will be shown on upper right.

Graph Feature
---

<img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_graph.png?token=AYaVqPVq2PehCoElV782Yy0Im_vHvkfhks5bvtLtwA%3D%3D" height="350"/> <img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_allTerms.png?token=AYaVqIOodkF2DZZBaJQHVYXNUDGol8fuks5bvtLxwA%3D%3D" height="350"/>

- A graph shows statistics about number of morized words, number of words tested, number of correct-and-incorrect counts in game.
- There are three time period options(weekly, monthly, and yearly)
- Under the graph, there is a list of all the words that a user memorized.


Profile Feature
---

<img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_moreActivity.png?token=AYaVqEq3bpoRMH0iGE8RituUFi3LG07qks5bvtMswA%3D%3D" height="350"/> <img src="https://raw.githubusercontent.com/ChangMinPark/mocket/develop/images/screenshot_editProfile.png?token=AYaVqAwI4imFBYStnya4fTY6se3dRBE7ks5bvtMkwA%3D%3D" height="350"/>

- On settings, users can set time duration for daily test notification and switch of vibration. 
- Users can edit their profile, logout, or completely delete their account.
