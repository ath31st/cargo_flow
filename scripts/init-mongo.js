db = db.getSiblingDB('cargo_flow_db');

db.createUser({
    user: "mongo",
    pwd: "mongo",
    roles: [
        {
            role: "readWrite",
            db: "cargo_flow_db"
        }
    ]
});

db.createCollection('statistics_collection');
