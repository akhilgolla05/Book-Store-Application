# Book-Store-Application
Manages the Book Store

>> sdk list java
>> sdk install java 21.0.2-tem
>> cat ~/.sdkman/etc/config
    >>sdkman_auto_env=true
> 
>>sdk list maven
>> sdk install maven 3.9.10
> 
> Wrappers to install maven/ gradle - dont need to install and setup, they just need to run wrapper
>> E:\Book Store Application\Book-Store-Application>mvn wrapper:wrapper
>  ./mvnw clean package 
> 
> mvn spotless:apply
$env:JAVA_HOME="C:\Program Files\Java\jdk-21"
$env:Path="$env:JAVA_HOME\bin;$env:Path" 