# Pre-work - *ToDo*

ToDo App is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: **Nidhi Singh**

Time spent: **10** hours spent in total

## User Stories

The following **required** functionality is completed:

1. User can **successfully add and remove items** from the todo list
2. User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
3. User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

1. Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
2.  Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
3. Add support for completion due dates for todo items (and display within listview item)
4. Add support for selecting the priority of each todo item (and display in listview item)
5. Tweak the style improving the UI / UX, play with colors, images or backgrounds

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://imgur.com/q1siC5F.gif' title='Video Walkthrough' width='600' alt='Video Walkthrough' />
GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."
1. The .xml and design files work hand in hand, if you are making some changes to .xml you can see that how it will look without running.
2. Fragments (although I dint use it in th current assignment, but read little bit here and here) are really cool, its like plug the view and resuse it.
3. Some things are complex, like recycler view adapters, inflate all looks tough terms, In iOS TableView(pretty much like recycler view) handles these things pretty neat, without developer knowing much details also.


**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."
I used ArrayAdapter in spinner.
To my understanding , it allows to link the view with its datasource, or it tells the view where to get data from.
It Returns a view for each object in a collection of data objects you provide. 
The convertView is the view of a row that left the screen(so it isn't the last view returned by the getView method). For example, the list is first shown, in this case convertView is null, no row view was previously built and left the screen. If you scroll down, row 0 will leave the screen(will not be visible anymore), when that happens the ListView may choose to keep that view in a cache to later use it.
So convertView is kind of caching which Android does for you.


## Notes

Describe any challenges encountered while building the app.
1. Recycler view is really complex view, took sometime to understand.
2. Understanding different layouts is fun yet tough to understand.
3. Making recycler view and getting data from sqlite and connecting them together.



## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
