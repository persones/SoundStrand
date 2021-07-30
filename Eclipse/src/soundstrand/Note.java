/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soundstrand;


public class Note implements Cloneable{
    public Note() {
        pitch = 60;
        time = 0;
        duration = 0.25f;
        looseness = 0.5f;
    }
    
    public Note(int inPitch, float inTime, float inDuration, float inLooseness)
    {
        pitch = inPitch;
        time = inTime;
        duration = inDuration;
        looseness = inLooseness;
    }
    
    public int getPitch() {
        return pitch;
    }
    
    public float getTime() {
        return time;
    }

    public float getDuration() {
        return duration;
    }
    
    public float getLooseness() {
    	return looseness;
    }
    
    public void setPitch(int p) {
        pitch = p;
    }
    
    public void setTime(float t) {
        time = t;
    }

    public void setDuration(float d) {
        duration = d;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    private int pitch;
    private float time;
    private float duration;
    private float looseness; 
}
