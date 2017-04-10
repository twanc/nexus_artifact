
import requests
import json

password = "admin123"
user = "admin"
url = "http://localhost:8081/"
scriptname = "test"
repoName = "test"
startDate = "2017-03-01"


with open('remove.groovy', 'rt') as sourceFile:
  res = sourceFile.read()

url += "service/siesta/rest/v1/script"
print url
payload = { "name": "test", "type": "groovy", "content": res }
print payload
headers = {'content-type': 'application/json'}
response = requests.delete(url+'/'+scriptname, headers=headers,auth=(user, password))
print response
response = requests.post(url, data=json.dumps(payload), headers=headers, auth=(user, password))
print response

headers = {'content-type': 'text/plain'}
payload = { "repoName": repoName, "startDate": startDate }
url = url+'/'+scriptname+'/run'
print url
response = requests.post(url, data=json.dumps(payload), headers=headers, auth=(user, password))
print response.content
print response
