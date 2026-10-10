-- People who can be assigned test plan items, identified by GitHub login (the curation sign-in).
-- Logins can differ from USERS.USERNAME, so the test plan keeps its own list.

CREATE TABLE test_plan_testers (
    github_login  VARCHAR(100) NOT NULL PRIMARY KEY,
    name          VARCHAR(200) NOT NULL
);

INSERT INTO test_plan_testers (github_login, name) VALUES
  ('adamgibs',      'Adam Gibson'),
  ('akwitek',       'Anne Kwitek'),
  ('jdepons',       'Jeff De Pons'),
  ('jrsjrs',        'Jennifer Smith'),
  ('jt15',          'Jyothi Thota'),
  ('llamersmcw',    'Logan Lamers'),
  ('tutajm',        'Marek Tutaj'),
  ('mlkaldunski',   'Mary Kaldunski'),
  ('meilbes',       'Missy Eilbes'),
  ('motutaj',       'Monika Tutaj'),
  ('shurjenw',      'Shur-Jen Wang'),
  ('szacher',       'Stacy Zacher'),
  ('wdemos',        'Wendy Demos')
ON CONFLICT (github_login) DO UPDATE SET name = EXCLUDED.name;

-- still to add once their GitHub logins are known: Kent Brodie, Varun Reddy Gollapally, Mindy Dwinell
