# Code Contributions and Code Reviews

#### Focused Commits

Grade: Very Good

Feedback: 
* Top: I see that some people are trying to be more consistent with your messages, and that you already started to implement some common naming conventions
* Tip: Make sure that everyone is on the same page with the commit names, as I see that Karol and Kenzo use the (Fix: / feature:/ tests:/ Bug:), 
but I couldn't see for everyone else.
* Top: I also see that some people added descriptions to thier commit messages; while this is great, if you think that you spend too much time on these,
you can also opt not to write them, especially when it can be summarized in the title.

#### Isolation

Grade: Good

Feedback: 
* Top: All features are developed in separate branches which are merged to main via a MR.
* Tip: You can create branches directly from the issues. In this way the names are more consistendt and descriptive.

#### Reviewability

Grade: Very Good

Feedback: 
* Top: I like that tou add labels to your MRs. This makes it easier for someone that reviews them.
* Top: This is an example of a good MR description: https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-19/-/merge_requests/26 ; it is very succint, it is clear wich are
the new features, which are the fixes and it is easy to read as it is formated as a list. Same for this one: https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-19/-/merge_requests/16
Same here: https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-19/-/merge_requests/34 . 
* Tip: Try being consistent with the descriptions, if you find a template that you lke, always use it to ensure consistency
and that when someone wants to review a MR, the structure of the description is always the same.
* Tip: Make sure you add the amount of time spent on the issue(s) in the MR desription: /estimate 2d -> gives 16h time spent: This MR has it for example: https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-19/-/merge_requests/34

#### Code Reviews

Grade: Good

Feedback: 
* Tip: Assign 2 reviewers for every MR (pick randomly or a person that worked on a similar feature). In this way, you make sure that everyone reviews, not only 2-3 people.
	* I'll use this MR [https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-19/-/merge_requests/34] again as example. It was reviewed by Kenzo (but he was not asssigned as reviewer - no
one was assigned for this MR), and Horia approved it without leaving any comments. 
* Top: A lot of comments on this [https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-19/-/merge_requests/26], which is good to see.
* Another good MR with reviews: https://gitlab.ewi.tudelft.nl/cse1105/2022-2023/teams/oopp-team-19/-/merge_requests/28

#### Build Server

Grade: Good
Feedback: 
* Great that checkstyle is completed and that you are using it at all times to ensure that the code follows the rules.
* Top: I can see that almost all branches pass the pipelines most of the times
* Tip: I can see that at some point, a MR with a failing pipeline was merged to main. Please do not let this happen again, as main remained
not fixed for at least a day as far as I can see. Also, do not allow the person creating the MR to also approve it, even if noone has time to review.
It is better to merge it next day than to have a lot of issues on main that were not spotted by a reviewer.

