# GitHub Best Practices 

## Workflow 

### Use the GitHub Flow 
The GitHub flow has 5 basic steps.
1. Create a branch
1. Make at least one commit
1. Open a Pull Request 
1. Collaborate and make more commits 
1. Merge and delete the branch

The GitHub Flow is streamlined for collaboration. Committing on branches other than `master` is safe, so experimental changes can be made without fear. Before merging the changes into `master`, code review and CI tests can happen right in the Pull Request. [See this interactive guide for more](https://guides.github.com/introduction/flow/).

### Branch often 
Branches in other version control systems can be heavy and burdensome. Branches are lightweight in Git. In Git, when you create a branch, it _feels_ like you're creating an entire copy of the repository, but in reality you're only creating a pointer.
Branch often and know that performance won't be affected negatively - you have unlimited sandboxes at your disposal to make changes in the most logical and fast way, not being limited by your tools.

### Small ships
Create small, iterative goals for your work. Create branches with defined purposes that can be merged quickly. Long living branches and pull requests get out of control and prohibit complete changes from being merged due to other, sometimes independant changes. Long living branches also have the potential to create more merge conflicts.
Branch often and merge often, and when there's more work to be done, don't be afraid to say "that's a good idea, let's create a different branch and PR for that."

### Open pull requests early 
It can be intimidating to work out in the open and show unfinished code. When you open a pull request early in your process, however, you do yourself and your team a favor by opening the work up for transparency and discussion. Open pull requests right when you start work on a branch, don't wait until the work is complete. 
Include the goals of the pull request and focus on the advantages of having more eyes on your code. 

## Use important files 
There are some "special files" with Git and GitHub. Some of the most important include: 

### `.gitignore`
A `.gitignore` file is a list of files that won't be tracked by Git. Git doesn't play well with binary files, so it's important to create a `.gitignore` as soon as possible and keep it up to date. Check out [these templates](https://github.com/github/gitignore) for ideas based on the types of files in your repository. 

### Issue templates
When someone opens an issue in a repository, by default there is only a blank slate. You can customize this, making it easier for contributors, stakeholders, and other people at your company to interact with you. 

Create a file named `.github/issue_template.md`. Fill in the issue template with the structure and information you would like to see in new issues opened in your repository. This way, when someone opens an issue, they'll see this template instead of an empty box.

### CODEOWNERS 
The `.github/CODEOWNERS` file is a place where you can define certain teams or individuals as "owners" of certain files or types of files. For example, you may want the `javascript` team to review any files ending with `.js`. Listing this in your `CODEOWNERS` file, along with using protected branches, will automatically assign that team as required reviewers for pull requests affecting Javascript files. 

## On GitHub 

### Write in Markdown
Markdown is a text formatting language. Files ending in `.md`, as well as all text in bodies of issues and pull requests, is formatted with markdown. Use markdown regularly to make your writing more readable. 

### Create and resolve issues
Communication is best when it's out in the open. Cross link frequently between related issues and pull requests to give context to current and future team members. Close issues by using [special keywords](https://help.github.com/articles/closing-issues-using-keywords/) in Pull Requests.

### Integrate with useful tools 
There are countless useful tools that integrate with GitHub and make working easier. One type is CI, or continuous integration, and is highly recommended. Implement CI early and prioritize quality code coverage. 

### Protect Branches 
While it's true that all things can be undone with Git, it's still a good idea to implement some levels of protection to your production code. Protected branches are used to ensure certain code doesn't get to the `master` branch without passing a set of qualifications first. The qualifications are flexible, and you define them, so you can choose the protections that are right for your project and your team.

## Git

### Write short but descriptive commit messages
Your commits should tell a story. Hopefully, you won't need to go back and look at your history other than to see what a great job you did. But if you ever have to find a mistake, clear commit messages make all the difference. They also make your code easier to maintain down the road, whether it's you or someone else maintaining it. 

### Don't change pushed commits
Once a commit has been pushed to the remote, don't change it! There are, of course, some circumstances when this is unavoidable (like sensitive information accidentally being committed and pushed). In that case, contact your administrator immediately. In general, if you need to change a commit that has been pushed to the remote, use `git revert`. `git revert` creates a _new_ commit and leaves existing history intact, unlike some other possibly destructive commands.

### Don't commit binary files
Binary files don't do well with Git, and are one of the only things that can negatively impact performance. A strong `.gitignore` can help prevent this. If you have already commited binary files, talk to your administrator, because some changing of history will probably be necessary. 

### Pull frequently
`git pull` updates your local repository. When you don't update your repository, you're missing work from other people on your team. When you don't pull frequently, you may run into the same problems you'd run into when using long running branches. There's a higher chance of merge conflicts, and there's a higher chance that you're spending time working on something that's already been done. 

### Commit often 
Remember, a good history is one that tells a story. It's not difficult to combine commits after they've been created, but it's hard to take one massive commit and sift through the changes to see what happened.
Small, frequent commits protect you from losing work. Small frequent commits also make it easier for you and your team to review the changes, and troubleshoot if something goes wrong.
Commits don't take up a lot of space with Git. 

## Gitting out of trouble 

If you think something's wrong, don't panic. Try these commands first: 

### `git status`
`git status` displays information to help you inspect your repository. It will give you information regarding the states of both the working directory and the staging area, as well as which files aren't being tracked by Git. Boiled down, `git status` tells you what's occurred with `git add` and `git commit`; it does not, however, give information regarding commited _history_ (for which you'd use `git log`). What should you look for when you type `git status`?

#### Current branch 
`git status` will always tell you what branch you are on. It's important to be on the correct branch before making commits. If you aren't on a branch, Git will tell you that too. You might be in the middle of a merge, in a detacthed-head state, or in the middle of another Git command. `git status` will always tell you.

![git status with no notable information](https://user-images.githubusercontent.com/9906718/35092065-87ac5908-fc3e-11e7-859f-13d8fecf9986.png)


#### Staging area and working directory
If there are changes in your working directory that haven't been committed to Git yet, `git status` will inform you about what those files are, and what their current status is. If they're in the working directory, untracked _or_ tracked, `git status` displays those files in red. 

![git status with files in working directory](https://user-images.githubusercontent.com/9906718/35092049-804c0e4c-fc3e-11e7-8bc9-41df893c8f20.png)

If the files are in the staging area and are ready to be committed, they'll be displayed in green.
![git status with files in staging area](https://user-images.githubusercontent.com/9906718/35092056-84b48f9a-fc3e-11e7-8283-6e6249f83ed5.png)

#### Up to date with the remote
`git status` also informs you about the relationship with your current local branch and the remote tracking branch. If you have commits locally that haven't been pushed, or if your local repository knows about changes on the remote that haven't been merged into your local branch, you'll see a message about it in `git status`. 

![git status with 1 unpushed commit](https://user-images.githubusercontent.com/9906718/35092063-86aea43e-fc3e-11e7-888a-b26dc5a3a645.png)

### `pwd`
`pwd` is short for "print working directory." Sometimes if something isn't looking right, you may not be where you think you are in your terminal. `pwd` will show you what directory you are currently in.

### `ls` (and `ls -al`)
`ls` will list the current files and directories that exist in the directory you're in. `ls -al` also shows hidden files. That can be particularly useful because `.git`, the file that differentiates a directory from a Git repository, is a hidden file.

### `git remote -v` 
If you are working with a remote or multiple remotes, your local repository has connections to those remotes. `git remote -v` lists out those remotes, along with the variable that usually refers to the remote. The default when cloning repositories is "origin." 
