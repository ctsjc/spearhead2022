
Issues: Why Outcome is not matching with expected.

OK Reduce your file...

NounPhrase|tag|Outcome
-|-|-
NP|NP|B-SP
and|cc|I-SP
VP|VP|I-SP
NP|NP|I-SP
NP|NP|B-SP
SP|SP|I-SP
PP|between|B-SP
SP|SP|I-SP
SBAR|than|I-SP
SP|SP|I-SP
PP|if|B-SP
SP|SP|I-SP
SP|SP|B-SP
PP|with|I-SP
NP|NP|I-SP
NP|NP|B-SP
PP|PP|I-SP
NP|NP|I-SP

Expected
`
SP-allows-NP-to transport-sp
`

Getting

NounPhrase|tag
-|-
SP|NP and spoke NP allows NP to transport NP between NP of NP with NP than if NP were served directly


NounPhrase|tag
-|-
NP	|	NP
and	|	CC
spoke	|	VP
NP	|	NP
allows	|	VP
NP	|	NP
to transport	|	VP
NP	|	NP
between	|	PP
NP	|	NP
of	|	PP
NP	|	NP
with	|	PP
NP	|	NP
than	|	SBAR
if	|	PP
NP	|	NP
were served directly	|	VP


```json
[
  {
    "tokens": [
      {
        "token": "NP",
        "pos": "NP",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "CC",
        "pos": "and",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "VP",
        "pos": "spoke",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "NP",
        "pos": "NP",
        "lemma": null,
        "chunk": null
      }
    ],
    "chunks": [
      "B-SP",
      "I-SP",
      "I-SP",
      "I-SP"
    ]
  },
  {
    "tokens": [
      {
        "token": "VP",
        "pos": "allows",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "NP",
        "pos": "NP",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "VP",
        "pos": "to transport",
        "lemma": null,
        "chunk": null
      }
    ],
    "chunks": [
      "B-SP",
      "I-SP",
      "I-SP"
    ]
  },
  {
    "tokens": [
      {
        "token": "NP",
        "pos": "NP",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "PP",
        "pos": "between",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "NP",
        "pos": "NP",
        "lemma": null,
        "chunk": null
      }
    ],
    "chunks": [
      "B-SP",
      "I-SP",
      "I-SP"
    ]
  },
  {
    "tokens": [
      {
        "token": "PP",
        "pos": "of",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "NP",
        "pos": "NP",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "PP",
        "pos": "with",
        "lemma": null,
        "chunk": null
      }
    ],
    "chunks": [
      "B-SP",
      "I-SP",
      "I-SP"
    ]
  },
  {
    "tokens": [
      {
        "token": "NP",
        "pos": "NP",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "SBAR",
        "pos": "than",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "PP",
        "pos": "if",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "NP",
        "pos": "NP",
        "lemma": null,
        "chunk": null
      },
      {
        "token": "VP",
        "pos": "were served",
        "lemma": null,
        "chunk": null
      }
    ],
    "chunks": [
      "B-SP",
      "I-SP",
      "I-SP",
      "I-SP",
      "I-SP"
    ]
  }
]
```