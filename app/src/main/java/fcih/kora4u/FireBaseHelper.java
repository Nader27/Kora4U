package fcih.kora4u;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class FireBaseHelper {
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference myRootRef = database.getReference();


    public interface OnGetDataListener<T> {
        void onSuccess(T Data);
    }

    public interface OnGetDataListListener<T> {
        void onSuccess(List<T> Data);
    }

    //region Temp
    private static class ClassName {
        //TODO:-----------------------------------------------------
        //TODO:Add Ref To Tables and make public class
        //ex: private static final DatabaseReference Ref = myRootRef.child("TableName");
        public static final DatabaseReference Ref = myRootRef.child("TableName");

        public String Key;
        //TODO:Add Columns
        //ex: public String Column;
        public String Column;
        //TODO:Add Foreign
        //ex:public ForeignClass ForeignClass

        public ClassName() {
            //TODO:ForeignClasses
            //ex ForeignClass = new ForeignClass();
        }

        //region Getter & Setter
        //TODO:Create Getter & Setter

        //endregion

        public String Add() {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            DatabaseReference myref = Ref.push();
            myref.setValue(Values);
            return Key = myref.getKey();

        }

        public void Add(String Key) {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            Ref.child(Key).setValue(Values);
        }

        public void Update() {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            Ref.child(Key).setValue(Values);
        }

        public void Update(String key) {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            Ref.child(key).setValue(Values);
        }

        public void Findbykey(String key, final OnGetDataListener<ClassName> listener) {
            Ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //TODO:Change ClassName
                    //ex: final ClassName obj = new Teams();
                    final ClassName obj = new ClassName();
                    obj.Key = dataSnapshot.getKey();
                    for (Table T : Table.values()) {
                        setbyName(obj, T.name(), dataSnapshot.child(T.text).getValue().toString());
                    }
                    //if no foreign key
                    listener.onSuccess(obj);
                    //TODO:Foreign Keys
                    //ex:
                    //ForeignClass.findbykey(obj.ForeignKey, new OnGetDataListener() {
                    //@Override
                    //public void onSuccess(Object Data) {
                    //    obj.ForeignClass = (FireBaseHelper.ForeignClass) Data;
                    //    listener.onSuccess(obj);
                    //}
                    //});
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void Where(Table table, String Value, final OnGetDataListListener<ClassName> listener) {
            //TODO:Change ClassName
            //ex: final List<ClassName> Items = new ArrayList<>();
            final List<ClassName> Items = new ArrayList<>();
            Query query = Ref.orderByChild(table.text).equalTo(Value);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Iterator iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot postSnapshot = (DataSnapshot) iterator.next();
                        //TODO:Change ClassName
                        //ex: final ClassName obj = new Teams();
                        final ClassName obj = new ClassName();
                        obj.Key = postSnapshot.getKey();
                        for (Table T : Table.values()) {
                            setbyName(obj, T.name(), postSnapshot.child(T.text).getValue().toString());
                        }
                        //if no foreign key
                        Items.add(obj);
                        if (!iterator.hasNext()) {
                            listener.onSuccess(Items);
                        }
                        //TODO:Foreign Keys
                        //ex:
                        //ForeignClass.findbykey(obj.ForeignKey, new OnGetDataListener() {
                        //@Override
                        //public void onSuccess(Object Data) {
                        //    obj.ForeignClass = (FireBaseHelper.ForeignClass) Data;
                        //    Items.add(obj);
                        //if (!iterator.hasNext()) {
                        //    listener.onSuccess(Items);
                        //}
                        //}
                        //});
                    }
                    if (dataSnapshot.getChildrenCount() == 0) {
                        listener.onSuccess(Items);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void Tolist(final OnGetDataListListener<ClassName> listener) {
            //TODO:Change ClassName
            //ex: final List<ClassName> Items = new ArrayList<>();
            final List<ClassName> Items = new ArrayList<>();
            Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    final Iterator iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot postSnapshot = (DataSnapshot) iterator.next();
                        //TODO:Change ClassName
                        //ex: final ClassName obj = new Teams();
                        final ClassName obj = new ClassName();
                        obj.Key = postSnapshot.getKey();
                        for (Table T : Table.values()) {
                            setbyName(obj, T.name(), postSnapshot.child(T.text).getValue().toString());
                        }
                        //if no foreign key
                        Items.add(obj);
                        if (!iterator.hasNext()) {
                            listener.onSuccess(Items);
                        }

                        //TODO:Foreign Keys
                        //ex:
                        //ForeignClass.findbykey(obj.ForeignKey, new OnGetDataListener() {
                        //@Override
                        //public void onSuccess(Object Data) {
                        //    obj.ForeignClass = (FireBaseHelper.ForeignClass) Data;
                        //    Items.add(obj);
                        //if (!iterator.hasNext()) {
                        //    listener.onSuccess(Items);
                        //}
                        //}
                        //});
                    }
                    if (dataSnapshot.getChildrenCount() == 0) {
                        listener.onSuccess(Items);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void Remove(String Key) {
            Ref.child(Key).removeValue();
        }

        //ToDo:Change ClassName
        private String getbyName(ClassName obj, String Name) {
            String Value = "";
            try {
                Method method = getClass().getDeclaredMethod("get" + Name);
                Object value = method.invoke(obj);
                Value = (String) value;

            } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return Value;
        }

        //ToDo:Change ClassName
        private void setbyName(ClassName obj, String Name, String Value) {
            try {
                Class[] cArg = new Class[1];
                cArg[0] = String.class;
                Method method = getClass().getDeclaredMethod("set" + Name, cArg);
                method.invoke(obj, Value);
            } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public enum Table {
            //TODO:Add Columns
            //ex:Column("ColumnName"),
            Column("ColumnName");

            private final String text;

            Table(final String text) {
                this.text = text;
            }

            @Override
            public String toString() {
                return text;
            }
        }
    }
    //endregion

    //region Matches_Table
    public static class Matches {

        //ex: private static final DatabaseReference Ref = myRootRef.child("TableName");
        public static final DatabaseReference Ref = myRootRef.child("Matches");

        //ex: public String ColumnName;
        public String Key;
        public String tournament;
        public String awayteam;
        public String hometeam;
        public String date;
        public String time;
        public String result;
        public Teams Awayteams;
        public Teams Hometeams;
        public Tournaments Tournaments;

        public Matches() {
            Awayteams = new Teams();
            Hometeams = new Teams();
            Tournaments = new Tournaments();
        }

        public Matches(String tournament, String awayteam, String hometeam, String date, String time, String result) {
            this.tournament = tournament;
            this.awayteam = awayteam;
            hometeam = hometeam;
            this.date = date;
            this.time = time;
            this.result = result;
        }

        //region Getter & setter

        public String getTournament() {
            return tournament;
        }

        public void setTournament(String tournament) {
            this.tournament = tournament;
        }

        public String getAwayteam() {
            return awayteam;
        }

        public void setAwayteam(String awayteam) {
            this.awayteam = awayteam;
        }

        public String getHometeam() {
            return hometeam;
        }

        public void setHometeam(String hometeam) {
            this.hometeam = hometeam;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }


        //endregion

        public String Add() {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            DatabaseReference myref = Ref.push();
            myref.setValue(Values);
            return Key = myref.getKey();

        }

        public void Add(String Key) {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.name(), getbyName(this, T.text));
            }
            Ref.child(Key).setValue(Values);
        }

        public void Update() {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            Ref.child(Key).setValue(Values);
        }

        public void Update(String key) {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            Ref.child(key).setValue(Values);
        }

        public void Findbykey(String key, final OnGetDataListener<Matches> listener) {
            Ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //ex: final Teams obj = new Teams();
                    final Matches obj = new Matches();
                    obj.Key = dataSnapshot.getKey();
                    for (Table T : Table.values()) {
                        setbyName(obj, T.name(), dataSnapshot.child(T.text).getValue().toString());
                    }
                    //if no foreign key
                    //listener.onSuccess(obj);
                    //ex:
                    Tournaments.Findbykey(obj.tournament, new OnGetDataListener() {
                        @Override
                        public void onSuccess(Object Data) {
                            obj.Tournaments = (FireBaseHelper.Tournaments) Data;
                            Awayteams.Findbykey(obj.awayteam, new OnGetDataListener() {
                                @Override
                                public void onSuccess(Object Data) {
                                    obj.Awayteams = (FireBaseHelper.Teams) Data;
                                    Hometeams.Findbykey(obj.hometeam, new OnGetDataListener() {
                                        @Override
                                        public void onSuccess(Object Data) {
                                            obj.Hometeams = (FireBaseHelper.Teams) Data;
                                            listener.onSuccess(obj);
                                        }
                                    });
                                }
                            });
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void Where(Table table, String Value, final OnGetDataListListener<Matches> listener) {
            //ex: final List<Teams> Items = new ArrayList<>();
            final List<Matches> Items = new ArrayList<>();
            Query query = Ref.orderByChild(table.text).equalTo(Value);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Iterator iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot postSnapshot = (DataSnapshot) iterator.next();
                        //ex: final Teams obj = new Teams();
                        final Matches obj = new Matches();
                        obj.Key = postSnapshot.getKey();
                        for (Table T : Table.values()) {
                            setbyName(obj, T.name(), postSnapshot.child(T.text).getValue().toString());
                        }
                        //if no foreign key
//                        Items.add(obj);
//                        if (!iterator.hasNext()) {
//                            listener.onSuccess(Items);
//                        }
                        Tournaments.Findbykey(obj.tournament, new OnGetDataListener() {
                            @Override
                            public void onSuccess(Object Data) {
                                obj.Tournaments = (FireBaseHelper.Tournaments) Data;
                                Awayteams.Findbykey(obj.awayteam, new OnGetDataListener() {
                                    @Override
                                    public void onSuccess(Object Data) {
                                        obj.Awayteams = (FireBaseHelper.Teams) Data;
                                        Hometeams.Findbykey(obj.hometeam, new OnGetDataListener() {
                                            @Override
                                            public void onSuccess(Object Data) {
                                                obj.Hometeams = (FireBaseHelper.Teams) Data;
                                                Items.add(obj);
                                                if (!iterator.hasNext()) {
                                                    listener.onSuccess(Items);
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    if (dataSnapshot.getChildrenCount() == 0) {
                        listener.onSuccess(Items);
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void Tolist(final OnGetDataListListener<Matches> listener) {
            //ex: final List<Teams> Items = new ArrayList<>();
            final List<Matches> Items = new ArrayList<>();
            Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    final Iterator iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot postSnapshot = (DataSnapshot) iterator.next();
                        //ex: final Teams obj = new Teams();
                        final Matches obj = new Matches();
                        obj.Key = postSnapshot.getKey();
                        for (Table T : Table.values()) {
                            setbyName(obj, T.name(), postSnapshot.child(T.text).getValue().toString());
                        }
                        //if no foreign key
//                        Items.add(obj);
//                        if (!iterator.hasNext()) {
//                            listener.onSuccess(Items);
//                        }
                        Tournaments.Findbykey(obj.tournament, new OnGetDataListener() {
                            @Override
                            public void onSuccess(Object Data) {
                                obj.Tournaments = (FireBaseHelper.Tournaments) Data;
                                Awayteams.Findbykey(obj.awayteam, new OnGetDataListener() {
                                    @Override
                                    public void onSuccess(Object Data) {
                                        obj.Awayteams = (FireBaseHelper.Teams) Data;
                                        Hometeams.Findbykey(obj.hometeam, new OnGetDataListener() {
                                            @Override
                                            public void onSuccess(Object Data) {
                                                obj.Hometeams = (FireBaseHelper.Teams) Data;
                                                Items.add(obj);
                                                if (!iterator.hasNext()) {
                                                    listener.onSuccess(Items);
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    if (dataSnapshot.getChildrenCount() == 0) {
                        listener.onSuccess(Items);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void Remove(String Key) {
            Ref.child(Key).removeValue();
        }

        private String getbyName(Matches obj, String Name) {
            String Value = "";
            try {
                Method method = getClass().getDeclaredMethod("get" + Name);
                Object value = method.invoke(obj);
                Value = (String) value;

            } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return Value;
        }

        private void setbyName(Matches obj, String Name, String Value) {
            try {
                Class[] cArg = new Class[1];
                cArg[0] = String.class;
                Method method = getClass().getDeclaredMethod("set" + Name, cArg);
                method.invoke(obj, Value);
            } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public enum Table {
            Tournament("tournament"),
            Awayteam("awayteam"),
            Hometeam("hometeam"),
            Date("date"),
            Time("time"),
            Result("result");

            private final String text;

            Table(final String text) {
                this.text = text;
            }

            @Override
            public String toString() {
                return text;
            }
        }
    }
    //endregion

    //region Teams
    public static class Teams {

        //ex: private static final DatabaseReference Ref = myRootRef.child("TableName");
        public static final DatabaseReference Ref = myRootRef.child("Teams");

        public String Key;
        //ex: public String Column;
        public String image;
        public String name;
        //ex:public ForeignClass ForeignClass

        public Teams() {
            //ex ForeignClass = new ForeignClass();
        }

        //region Getter & Setter

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        //endregion

        public String Add() {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            DatabaseReference myref = Ref.push();
            myref.setValue(Values);
            return Key = myref.getKey();

        }

        public void Add(String Key) {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            Ref.child(Key).setValue(Values);
        }

        public void Update() {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            Ref.child(Key).setValue(Values);
        }

        public void Update(String key) {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            Ref.child(key).setValue(Values);
        }

        public void Findbykey(String key, final OnGetDataListener<Teams> listener) {
            Ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //ex: final Teams obj = new Teams();
                    final Teams obj = new Teams();
                    obj.Key = dataSnapshot.getKey();
                    for (Table T : Table.values()) {
                        setbyName(obj, T.name(), dataSnapshot.child(T.text).getValue().toString());
                    }
                    //if no foreign key
                    listener.onSuccess(obj);
                    //ex:
                    //ForeignClass.findbykey(obj.ForeignKey, new OnGetDataListener() {
                    //@Override
                    //public void onSuccess(Object Data) {
                    //    obj.ForeignClass = (FireBaseHelper.ForeignClass) Data;
                    //    listener.onSuccess(obj);
                    //}
                    //});
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void Where(Table table, String Value, final OnGetDataListListener<Teams> listener) {
            //ex: final List<Teams> Items = new ArrayList<>();
            final List<Teams> Items = new ArrayList<>();
            Query query = Ref.orderByChild(table.text).equalTo(Value);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Iterator iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot postSnapshot = (DataSnapshot) iterator.next();
                        //ex: final Teams obj = new Teams();
                        final Teams obj = new Teams();
                        obj.Key = postSnapshot.getKey();
                        for (Table T : Table.values()) {
                            setbyName(obj, T.name(), postSnapshot.child(T.text).getValue().toString());
                        }
                        //if no foreign key
                        Items.add(obj);
                        if (!iterator.hasNext()) {
                            listener.onSuccess(Items);
                        }
                        //ex:
                        //ForeignClass.findbykey(obj.ForeignKey, new OnGetDataListener() {
                        //@Override
                        //public void onSuccess(Object Data) {
                        //    obj.ForeignClass = (FireBaseHelper.ForeignClass) Data;
                        //    Items.add(obj);
                        //if (!iterator.hasNext()) {
                        //    listener.onSuccess(Items);
                        //}
                        //}
                        //});
                    }
                    if (dataSnapshot.getChildrenCount() == 0) {
                        listener.onSuccess(Items);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void Tolist(final OnGetDataListListener<Teams> listener) {
            //ex: final List<Teams> Items = new ArrayList<>();
            final List<Teams> Items = new ArrayList<>();
            Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    final Iterator iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot postSnapshot = (DataSnapshot) iterator.next();
                        //ex: final Teams obj = new Teams();
                        final Teams obj = new Teams();
                        obj.Key = postSnapshot.getKey();
                        for (Table T : Table.values()) {
                            setbyName(obj, T.name(), postSnapshot.child(T.text).getValue().toString());
                        }
                        //if no foreign key
                        Items.add(obj);
                        if (!iterator.hasNext()) {
                            listener.onSuccess(Items);
                        }
                        //ex:
                        //ForeignClass.findbykey(obj.ForeignKey, new OnGetDataListener() {
                        //@Override
                        //public void onSuccess(Object Data) {
                        //    obj.ForeignClass = (FireBaseHelper.ForeignClass) Data;
                        //    Items.add(obj);
                        //if (!iterator.hasNext()) {
                        //    listener.onSuccess(Items);
                        //}
                        //}
                        //});
                    }
                    if (dataSnapshot.getChildrenCount() == 0) {
                        listener.onSuccess(Items);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void Remove(String Key) {
            Ref.child(Key).removeValue();
        }

        private String getbyName(Teams obj, String Name) {
            String Value = "";
            try {
                Method method = getClass().getDeclaredMethod("get" + Name);
                Object value = method.invoke(obj);
                Value = (String) value;

            } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return Value;
        }

        private void setbyName(Teams obj, String Name, String Value) {
            try {
                Class[] cArg = new Class[1];
                cArg[0] = String.class;
                Method method = getClass().getDeclaredMethod("set" + Name, cArg);
                method.invoke(obj, Value);
            } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public enum Table {
            //ex:Column("ColumnName"),
            Image("image"),
            Name("name");

            private final String text;

            Table(final String text) {
                this.text = text;
            }

            @Override
            public String toString() {
                return text;
            }
        }
    }
    //endregion

    //region Tournaments
    public static class Tournaments {

        //ex: private static final DatabaseReference Ref = myRootRef.child("TableName");
        public static final DatabaseReference Ref = myRootRef.child("Tournaments");

        public String Key;
        //ex: public String Column;
        public String name;
        //ex:public ForeignClass ForeignClass

        public Tournaments() {
            //ex ForeignClass = new ForeignClass();
        }

        //region Getter & Setter

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        //endregion

        public String Add() {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            DatabaseReference myref = Ref.push();
            myref.setValue(Values);
            return Key = myref.getKey();

        }

        public void Add(String Key) {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            Ref.child(Key).setValue(Values);
        }

        public void Update() {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            Ref.child(Key).setValue(Values);
        }

        public void Update(String key) {
            Map<String, String> Values = new HashMap<>();
            for (Table T : Table.values()) {
                Values.put(T.text, getbyName(this, T.name()));
            }
            Ref.child(key).setValue(Values);
        }

        public void Findbykey(String key, final OnGetDataListener<Tournaments> listener) {
            Ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //ex: final ClassName obj = new Teams();
                    final Tournaments obj = new Tournaments();
                    obj.Key = dataSnapshot.getKey();
                    for (Table T : Table.values()) {
                        setbyName(obj, T.name(), dataSnapshot.child(T.text).getValue().toString());
                    }
                    //if no foreign key
                    listener.onSuccess(obj);
                    //ex:
                    //ForeignClass.findbykey(obj.ForeignKey, new OnGetDataListener() {
                    //@Override
                    //public void onSuccess(Object Data) {
                    //    obj.ForeignClass = (FireBaseHelper.ForeignClass) Data;
                    //    listener.onSuccess(obj);
                    //}
                    //});
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void Where(Table table, String Value, final OnGetDataListListener<Tournaments> listener) {
            //ex: final List<ClassName> Items = new ArrayList<>();
            final List<Tournaments> Items = new ArrayList<>();
            Query query = Ref.orderByChild(table.text).equalTo(Value);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Iterator iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot postSnapshot = (DataSnapshot) iterator.next();
                        //ex: final ClassName obj = new Teams();
                        final Tournaments obj = new Tournaments();
                        obj.Key = postSnapshot.getKey();
                        for (Table T : Table.values()) {
                            setbyName(obj, T.name(), postSnapshot.child(T.text).getValue().toString());
                        }
                        //if no foreign key
                        Items.add(obj);
                        if (!iterator.hasNext()) {
                            listener.onSuccess(Items);
                        }
                        //ex:
                        //ForeignClass.findbykey(obj.ForeignKey, new OnGetDataListener() {
                        //@Override
                        //public void onSuccess(Object Data) {
                        //    obj.ForeignClass = (FireBaseHelper.ForeignClass) Data;
                        //    Items.add(obj);
                        //if (!iterator.hasNext()) {
                        //    listener.onSuccess(Items);
                        //}
                        //}
                        //});
                    }
                    if (dataSnapshot.getChildrenCount() == 0) {
                        listener.onSuccess(Items);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void Tolist(final OnGetDataListListener<Tournaments> listener) {
            //ex: final List<ClassName> Items = new ArrayList<>();
            final List<Tournaments> Items = new ArrayList<>();
            Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    final Iterator iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot postSnapshot = (DataSnapshot) iterator.next();
                        //ex: final ClassName obj = new Teams();
                        final Tournaments obj = new Tournaments();
                        obj.Key = postSnapshot.getKey();
                        for (Table T : Table.values()) {
                            setbyName(obj, T.name(), postSnapshot.child(T.text).getValue().toString());
                        }
                        //if no foreign key
                        Items.add(obj);
                        if (!iterator.hasNext()) {
                            listener.onSuccess(Items);
                        }
                        //ex:
                        //ForeignClass.findbykey(obj.ForeignKey, new OnGetDataListener() {
                        //@Override
                        //public void onSuccess(Object Data) {
                        //    obj.ForeignClass = (FireBaseHelper.ForeignClass) Data;
                        //    Items.add(obj);
                        //if (!iterator.hasNext()) {
                        //    listener.onSuccess(Items);
                        //}
                        //}
                        //});
                    }
                    if (dataSnapshot.getChildrenCount() == 0) {
                        listener.onSuccess(Items);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void Remove(String Key) {
            Ref.child(Key).removeValue();
        }

        private String getbyName(Tournaments obj, String Name) {
            String Value = "";
            try {
                Method method = getClass().getDeclaredMethod("get" + Name);
                Object value = method.invoke(obj);
                Value = (String) value;

            } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return Value;
        }

        private void setbyName(Tournaments obj, String Name, String Value) {
            try {
                Class[] cArg = new Class[1];
                cArg[0] = String.class;
                Method method = getClass().getDeclaredMethod("set" + Name, cArg);
                method.invoke(obj, Value);
            } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public enum Table {
            //ex:Column("ColumnName"),
            Name("name");

            private final String text;

            Table(final String text) {
                this.text = text;
            }

            @Override
            public String toString() {
                return text;
            }
        }
    }
    //endregion

}
