## DATABASE:

#### apprentices
- id
- name  
  
#### apprentice_scorecards
- id
- taken_at
- total
- correct
- apprentice_id

#### apprentice_scores
- id
- scorecard_id
- question_number
- correct
- edge_id
- graph_id
- apprentice_id

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
- apprentice_id

#### emotions
- id
- name
- label_id
- apprentice_id ?

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

#### key_signatures
- id
- emotion_id
- apprentice_id

#### key_notes
- id
- key_signature_id
- notevalue
- apprentice_id

#### vertices
- id
- graph_id
- node
- apprentice_id