import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Collection;

/**
 * A class to maintain an arbitrary number of contact details.
 * Details are indexed by both name and phone number.
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version 2016.02.29
 */
public class AddressBook
{
    // Storage for an arbitrary number of details.
    private TreeMap<String, ContactDetails> book;
    private int numberOfEntries;

    /**
     * Perform any initialization for the address book.
     */
    public AddressBook()
    {
        book = new TreeMap<>();
        numberOfEntries = 0;
    }
    
    /**
     * Look up a name or phone number and return the
     * corresponding contact details.
     * @param key The name or number to be looked up.
     * @return The details corresponding to the key.
     */
    public ContactDetails getDetails(String key)
    {
        return book.get(key);
    }

    /**
     * Return whether or not the current key is in use.
     * @param key The name or number to be looked up.
     * @return true if the key is in use, false otherwise.
     */
    public boolean keyInUse(String key)
    {
        return book.containsKey(key);
    }

    /**
     * Add a new set of details to the address book.
     * @param details The details to associate with the person.
     */
    public void addDetails(ContactDetails details) throws DuplicateKeyException
    {
        if(details == null) {
            throw new IllegalArgumentException("Null details passed to addDetails.");
        }
        else if(book.containsKey(details.getName())) {
            throw new DuplicateKeyException();
        }
        book.put(details.getName(), details);
        book.put(details.getPhone(), details);
        numberOfEntries++;
    }
    
    /**
     * Change the details previously stored under the given key.
     * @param oldKey One of the keys used to store the details.
                     This should be a key that is currently in use.
     * @param details The replacement details. Must not be null.
     * @throws IllegalArgumentException If either argument is null.
     */
    public void changeDetails(String oldKey,
                                ContactDetails details) throws NoMatchingDetailsException
    {
        if(details == null) {
            throw new IllegalArgumentException("Null details passed to changeDetails.");
        }
        if(oldKey == null){
            throw new IllegalArgumentException("Null key passed to changeDetails.");
        }
        if(keyInUse(oldKey)){
            removeDetails(oldKey);
            try
            {
                addDetails(details);   
            } 
            catch(DuplicateKeyException dke)
            {
                System.out.println("Key already defined in this address book");
            }
        } else{
            throw new NoMatchingDetailsException(oldKey);
        }
    }
    
    /**
     * Search for all details stored under a key that starts with
     * the given prefix.
     * @param keyPrefix The key prefix to search on. This may be
     *                  of zero length, but must not be null.
     * @return An array of those details that have been found.
     */
    public ContactDetails[] search(String keyPrefix)
    {
        // Build a list of the matches.
        List<ContactDetails> matches = new LinkedList<>();
        if(keyPrefix != null) {
            // Find keys that are equal-to or greater-than the prefix.
            SortedMap<String, ContactDetails> tail = book.tailMap(keyPrefix);
            Iterator<String> it = tail.keySet().iterator();
            // Stop when we find a mismatch.
            boolean endOfSearch = false;
            while(!endOfSearch && it.hasNext()) {
                String key = it.next();
                if(key.startsWith(keyPrefix)) {
                    matches.add(book.get(key));
                }
                else {
                    endOfSearch = true;
                }
            }
        }
        ContactDetails[] results = new ContactDetails[matches.size()];
        matches.toArray(results);
        return results;
    }

    /**
     * Return the number of entries currently in the
     * address book.
     * @return The number of entries.
     */
    public int getNumberOfEntries()
    {
        return numberOfEntries;
    }

    /**
     * Remove the entry with the given key from the address book.
     * The key should be one that is currently in use.
     * @param key One of the keys of the entry to be removed.
     * @throws IllegalArgumentException If the key is null.
     */
    public void removeDetails(String key) throws NoMatchingDetailsException
    {
        if(key == null){
            throw new IllegalArgumentException("Null key passed to removeDetails.");
        }
        if(keyInUse(key)) {
            ContactDetails details = book.get(key);
            book.remove(details.getName());
            book.remove(details.getPhone());
            numberOfEntries--;
        }
        else
        {
            throw new IllegalArgumentException("Key "+key+" does not have an adress atached");
        }
    }
    /**
     * Return all the contact details, sorted according
     * to the sort order of the ContactDetails class.
     * @return A sorted list of the details.
     */
    public String listDetails()
    {
        // Because each entry is stored under two keys, it is
        // necessary to build a set of the ContactDetails. This
        // eliminates duplicates.
        StringBuilder allEntries = new StringBuilder();
        Set<ContactDetails> sortedDetails = new TreeSet<>(book.values());
        for(ContactDetails details : sortedDetails) {
            allEntries.append(details).append("\n\n");
        }
        return allEntries.toString();
    }
    
    private void createFile(String filename)
    {   File contacts = new File(filename);
        FileWriter fw = new FileWriter(contacts); 
        Collection c = book.values();
        Iterator it = new c.iterartor();
        /*for(ContactDetails cd: book)
        {
            fw.append(cd.toString());
        }*/
        
        while(it.hasNext())
        {
            
        }
    }
    
}
