# Simple regex generator
[![](https://api.travis-ci.com/atnakenek/regex-generator.svg?branch=master)](https://app.travis-ci.com/github/Atnakenek/regex-generator) ![](https://img.shields.io/github/stars/atnakenek/regex-generator.svg) ![](https://img.shields.io/github/tag/atnakenek/regex-generator.svg) ![](https://img.shields.io/github/forks/atnakenek/regex-generator.svg)  ![](https://img.shields.io/github/release/atnakenek/regex-generator.svg) ![](https://img.shields.io/github/issues/atnakenek/regex-generator.svg)

Given a list of alphanumeric codes, returns the best-matching regular expression for all of them.

### Examples
1. Given a list of italian car plates numbers:
   AB123ZZ
   BB742TG
   CF678HG
   ...
   resulting regular expression is: `[A-Z]{2}\d{3}[A-Z]{2}`

2. Given a list of italian fiscal codes:
   TNTTST80A01F205E
   ...
   resulting regular expression is: `[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]`

3. Given the following codes:
   AA123
   BA1234
   AB12345
   resulting regular expression is: `[A-Z]{2}\d{3,5}`

4. Given the following codes:
   A123XY
   BA1234ZT
   AB12345B
   resulting regular expression is: `[A-Z]{1,2}\d{3,5}[A-Z]{1,2}`

### Description
It is a SpringBoot application responding to the REST endpoint [POST] `/api/regex/generate`.
##### Request
```json
{
    "codes": [
        "AB123ZZ",
        "BB742TG",
		"CF678HG"
    ]
}
```
##### Response
```json
{
    "regex": "[A-Z]{2}\\d{3}[A-Z]{2}"
}
```

### Notes
- Supported only upper case characters and digits (as per prerequisite), although its implementation allows to include lower case characters or other types as well.
- deployed on Heroku:  `https://regex-gen.herokuapp.com/api/regex/generate`