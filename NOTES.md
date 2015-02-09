## NOTES:
- approaches to try first for generating music:
  - Approach 1:
    - gather related notes from database based on given emotion
    - convert query results into individual "noteset bundles"
    - randomly choose from list of "noteset bundles"
  - Approach 2:
    - randomly query database for notesets and notes based on given emotion
- serializedValue is a serialized value of notesets (with the following properties):
  notevalue:velocity:length:position|
- Playback Speed for Visualization:
  - 120x = 0.035f * 60
  - x = (0.035*60) / 120
- may try calculating Apprentice strongest paths in a separate table
  - possibly use another thread to do this path finding