# otashu collection

## TODO:
- pick random notesets from gathered data structure
- write chosen notesets to midi file
- playback midi file
- add spinners for length in CreateNotesetActivity
- add spinners for velocity in CreateNotesetActivity


## DONE:
- 20140909: added note gathering feature for selecting notesets that match a user-selected emotion
- 20140909: added emotion_id to notesets table in database
- 20140908: added ChooseEmotionActivity
- 20140907: added toasts to database import/export process
- 20140906: added database import feature
- 20140905: added database export feature
- 20140903: decide on table structure for emotion id (decision for now: if SQLite really does support `WHERE IN ()` then tagged emotions can be stored as ids in noteset table rows... and tags can be searched for using numbers rather than strings)
- 20140902: create new noteset + create related notes in database
- 20140901: separate out notesets and notes
- 20140901: separate database opener and data source methods


## DATABASE:
#### emotions
- id
- name

#### notes
- id
- noteset_id
- notevalue
- velocity
- length

#### notesets
- id
- name


## NOTES:
- approaches to try first for generating music:
  - Approach 1:
    - gather related notes from database based on given emotion
    - convert query results into individual "noteset bundles"
    - randomly choose from list of "noteset bundles"
  - Approach 2:
    - randomly query database for notesets and notes based on given emotion