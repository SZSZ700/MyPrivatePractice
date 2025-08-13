package org.example;
import java.util.Stack;

// pyramid of clowns class
public class Spyramid {
    // stack of clowns
    private Stack<Clown> pyramid;

    // constructor - create empty pyramid
    public Spyramid() { this.pyramid = new Stack<>(); }

    // getter
    public Stack<Clown> getPyramid() { return pyramid; }

    // setter
    public void setPyramid(Stack<Clown> pyramid) { this.pyramid = pyramid; }

    // method to check if pyramid id stable
    // the lightest one found on top while the heaviest one found in the bottom
    public boolean isStable(){
        // restoration clowns stack
        var restoration = new Stack<Clown>();

        // previous clown weight
        var prev = this.pyramid.peek().getWeight();
        //push current/prev clown to the restoration stack
        restoration.push(this.pyramid.pop());

        // for identicate if pyramid is not valid
        var notValid  = false;

        // iteration - on the original stack
        while (!this.pyramid.empty()){
            // peek current value (current clown weight)
            var current = this.pyramid.peek().getWeight();
            // push current clown to the restoration stack
            restoration.push(this.pyramid.pop());

            // if this clown not heavier than the previous clown, the pyramid not valid
            if (current < prev){ notValid = true; }

            // prev weight = current weight
            prev = current;
        }

        //restoration  - restore the original stack
        while (!restoration.empty()){ this.pyramid.push(restoration.pop()); }

        // if its indicated not Valid pyramid, return false, else return true
        return notValid? false : true;
    }

    // methoud that try to add clown to the pyramid, if its allready stable
    public boolean addClown(Clown c){
        // if pyramid not stable, return false
        if (!isStable()){ return false; }

        // add on top - if the new clown is lightest than the clown on top
        if (c.getWeight() < this.pyramid.peek().getWeight()){
            // add the new clown to the pyramid
            this.pyramid.push(c);
            // return true - the new clown was addedd successfully
            return true;
        }

        // restoration stack
        var restoration = new Stack<Clown>();

        // the weight of the current clown
        var prev = this.pyramid.peek().getWeight();
        // push the current clown to the restoration stack
        restoration.push(this.pyramid.pop());

        // add between and or in the end (if needed)
        while (!this.pyramid.empty()){
            // the weight of the current clown
            var current = this.pyramid.peek().getWeight();

            // if the new clown heaviest than prevoius clown,
            // and lightest that the current clown
            if (c.getWeight() > prev && c.getWeight() < current){
                restoration.push(c); // push the new clown to the restoration stack
                break;
            }

            // push the current clown to the restoration stack if i cant push the new clown
            // between them
            restoration.push(this.pyramid.pop());

            prev = current;
        }

        // restore the original stack
        while (!restoration.empty()){ this.pyramid.push(restoration.pop()); }

        // return true - the new clown was addedd successfully
        return true;
    }


    // print class details
    @Override
    public String toString() { return "Spyramid{" + "pyramid=" + pyramid + '}'; }
}
