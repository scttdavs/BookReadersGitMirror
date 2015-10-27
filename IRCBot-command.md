#Available commands for IRCbots:
####Schedule a job build, with standard, custom or no quiet period
    build <job> [now|<delay>[s|m|h]] [<parameterkey>=<value>]* 
    schedule <job> [now|<delay>[s|m|h]] [<parameterkey>=<value>]* 
####Specify which job to abort
    abort <job>
####Adds a description to a build
    comment <job> <build-#> <comment>

---
####List jobs which are currently in progress
    cb 
    currentlyBuilding 
####Show the health of a specific job, jobs in a view or all jobs
    h [<job>|-v <view>]
    health [<job>|-v <view>]
####Show the status of a specific job, jobs in a view or all jobs
    jobs [<job>|-v <view>] 
    s [<job>|-v <view>]
    status [<job>|-v <view>]
####Show the state of the build queue
    q
    queue
####Show the test results of a specific job, jobs in a view or all jobs
    testresult [<job>|-v <view>]
####Prints information about a Jenkins user
    userstat <username> 

---
####Defines a new alias, deletes one or lists all existing aliases
    alias [<alias> [<command>]] 
####om nom nom
    botsnack [<snack>]