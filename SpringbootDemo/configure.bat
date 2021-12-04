@echo off&setlocal EnableDelayedExpansion
chcp 65001
set ori_resources=C:/Users/Owen Liu/Desktop/project/backend-seiiiassignment/SpringbootDemo/src/main/resources
set resources=%cd:\=/%/src/main/resources
set ori_password=owen890628
set password=%1
set curdir=%~dp0
for /f "delims=" %%b in ('type "%curdir%src\main\resources\hanlp-template.properties"') do (
set "str=%%b"&set "str=!str:%ori_resources%=%resources%!"
echo !str!>>"%curdir%src\main\resources\hanlp.properties"
)
for /f "delims=" %%b in ('type "%curdir%src\main\resources\application-template.yml"') do (
set "str=%%b"&set "str=!str:%ori_password%=%password%!"
echo !str!>>"%curdir%src\main\resources\application.yml"
)