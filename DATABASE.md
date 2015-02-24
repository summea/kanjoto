## DATABASE:
#### apprentice_scorecards
- id
- taken_at
- total
- correct

#### apprentice_scores
- id
- scorecard_id
- question_number
- correct
- edge_id
- graph_id

#### bookmarks
- id
- name
- serialized_value

#### edges
- id
- graph_id
- emotion_id
- from_node_id
- to_node_id
- weight
- position

#### emotions
- id
- name
- label_id

#### graphs
- id
- name

#### labels
- id
- name
- color

#### notes
- id
- noteset_id
- notevalue
- velocity
- length
- position

#### notesets
- id
- name
- enabled

#### notevalues
- id
- notevalue
- notelabel
- label_id

#### vertices
- id
- graph_id
- node