# Advanced-Android

Running guide:

1. make sure you follow the read me at https://github.com/Teananazz/AdvancedPart2/tree/android for the server side.
2.  firebase will only work properly on phones with google play(didn't test it completely.)
3.  make sure tools -> fire base -> Cloud messaging is properly connected ( 1 and 2 should be checked marked)
4. img should be not be weighting more than 300kb( not tested more than that - but crashing at 1.2Mb)

got permission from Yotam to add this to the document:

we put the server changing button after the login, as the consequence of this after you change server fire base will not work almost certainly.
Therfore, if you want to change server, change manually BASE_URL in RetrofitClient.java and restart and run the app again from the beginning.
