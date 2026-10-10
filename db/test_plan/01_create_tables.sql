-- Site test plan for the Oracle -> PostgreSQL switch: one row per page or tool to test,
-- with its assignment, result and sign-off. Read and written by TestPlanDAO.

CREATE TABLE test_plan_items (
    item_id            VARCHAR(80)  NOT NULL PRIMARY KEY,
    area               VARCHAR(100) NOT NULL,
    name               VARCHAR(200) NOT NULL,
    path               VARCHAR(500) NOT NULL,   -- relative to the site root, e.g. /rgdweb/report/gene/main.html?id=2004
    check_text         TEXT,                    -- what to check on this item
    covers             TEXT,                    -- other URLs / functions this item covers
    curation           CHAR(1)      DEFAULT 'N' NOT NULL,   -- Y: curation build or PHP curation tool only
    no_compare         CHAR(1)      DEFAULT 'N' NOT NULL,   -- Y: no production page to compare with
    sort_order         INTEGER      NOT NULL,
    assignee           VARCHAR(100),            -- GitHub login (users.username)
    status             VARCHAR(20)  DEFAULT 'Not started' NOT NULL,
    notes              TEXT,
    signed_by          VARCHAR(100),            -- GitHub login of the tester who signed off
    signed_date        TIMESTAMP,
    last_modified_by   VARCHAR(100),
    last_modified_date TIMESTAMP,
    CONSTRAINT test_plan_items_status_ck
        CHECK (status IN ('Not started', 'In progress', 'Passed', 'Failed', 'Blocked'))
);

-- every change to an item: who, when, what
CREATE TABLE test_plan_history (
    history_key  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_id      VARCHAR(80)  NOT NULL REFERENCES test_plan_items (item_id),
    changed_by   VARCHAR(100) NOT NULL,
    change_date  TIMESTAMP    NOT NULL,
    action       VARCHAR(4000) NOT NULL
);

CREATE INDEX test_plan_history_item_ix ON test_plan_history (item_id, change_date);
