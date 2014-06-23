INSERT INTO core_dashboard (dashboard_name, dashboard_column, dashboard_order) VALUES ('DISPLAY_STATUS_HIPOLITE', 4, 1 );
INSERT INTO core_dashboard (dashboard_name, dashboard_column, dashboard_order) VALUES ('CHANGE_STATUS_HIPOLITE', 1, 5 );
UPDATE core_dashboard SET dashboard_order = 2 WHERE dashboard_name = 'CORE_USER';

INSERT INTO core_datastore VALUES ('hipolite.sync_statut', '0');
