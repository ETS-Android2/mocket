package com.rms.mocket.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rms.mocket.object.Term;

public class FirebaseHandlerTerm {

    public static void addTerm(String term, String definition){
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mTermDatabase =
                FirebaseDatabase.getInstance().getReference(Term.REFERENCE_TERMS);
        String term_id = mTermDatabase.child(user_id).push().getKey();
        mTermDatabase.child(user_id).child(term_id).setValue(new Term(term_id, term, definition));
    }


    public static void updateTerm(String id, Term term){
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mTermDatabase =
                FirebaseDatabase.getInstance().getReference(Term.REFERENCE_TERMS);
        mTermDatabase.child(user_id).child(id).setValue(term);
    }

    public static void deleteTerm(String id){
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mTermDatabase =
                FirebaseDatabase.getInstance().getReference(Term.REFERENCE_TERMS);
        mTermDatabase.child(user_id).child(id).removeValue();
    }

}
