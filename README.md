# azure-function-kotlin

 mvn clean package
 
 mvn azure-functions:run
 
 mvn azure-functions:run -DenableDebug
 
 mvn azure-functions:deploy
 
 terraform init --backend-config="terraform.cfg"
 
 terraform apply --var-file="terraform.tvars"

# Parser
Thanks for this contribution from Joe and Alphine
```shell
 for x in {a..z}; do echo "{\"words\": [" >$x.json; cat dictionary.csv | grep -i "^\"[$x]" | awk -F "\"" {'print "{""\"word\"" ":" "\""$2"\"," "\"class\"" ":" "\""$4"\"," "\"meaning\"" ":" "\""$6"\"," "\"search\"" ":" "\""tolower($2)"\"""},"'}  >> $x.json; sed -i '' -e '$s/\,$//g' $x.json; echo  "],\"length\":$(cat dictionary.csv|grep -i "^\"[$x]" | wc -l),\"tag\": \"$x\"}" >> $x.json;done
```
 
 
Link:
https://docs.microsoft.com/en-us/azure/azure-functions/functions-create-maven-intellij


