#include "UCharList.h"
// constructor()
UCharList::UCharList(list<unique_ptr<char>> &otherList) {
    chain.splice(this->chain.begin(), otherList);
}

// swap()
void UCharList::swap(const char letter) {

    // Lambda function that searches for a specific character inside a given list
    // and returns iterator to the BinNode that contains it
    [[maybe_unused]]auto findAfter =
    [&](
        // the given list
        list<unique_ptr<char>>& otherList,
        // iterator to some binnode
        const list<unique_ptr<char>>::iterator it,
        // the charcter that should be in some binnode
        const char letterr
        ) -> list<unique_ptr<char>>::iterator {

        // Start from the next node after the given iterator
        // Traverse until end
        for (auto iterator = next(it); iterator != otherList.end(); ++iterator) {
            // Check value
            // ReSharper disable once CppDFALocalValueEscapesFunction
            if (**iterator == letterr) { return iterator; }
        }
        return otherList.end(); // Not found
    };

    // Lambda function that returns iterator to the last binnode
    // that contains the letter
    [[maybe_unused]] auto last =
        [&](
            // the given list
            list<unique_ptr<char>>& chain,
            // the character that should be in some binnode
            const char letterr
            ) -> list<unique_ptr<char>>::iterator {

            // Traverse from end to start
            for (auto it = chain.rbegin(); it != chain.rend(); ++it) {
                // Check value
                if (**it == letterr) {
                    // Convert reverse_iterator to the correct forward iterator
                    return prev(it.base());
                }
            }
            return chain.end(); // Not found
    };

    // iterator to the first binnode
    const auto start = this->chain.begin();
    // iterator to the first binnode that contains the letter
    const auto firstOc = findAfter(this->chain, start, letter);
    // iterator to the last binnode that contains the letter
    const auto lastOc = last(this->chain, letter);
    // iterator to the last binnode
    const auto tail = this->chain.end();
    // iterator to the binnode after the last binnode that contains the letter
    const auto afterLastOc = next(lastOc);

    // Temporary list for the prefix
    list<unique_ptr<char>> temp;

    // chain:  A → B → [X] → C → D → [X] → E → F //

    // Move the prefix [begin, firstOc) into the temporary list
    // temp:   A → B //
    // chain:  X → C → D → X → E → F //
    temp.splice(temp.end(), this->chain, start, firstOc);

    // Move the suffix [afterLastOc, end) to the beginning of the original list
    // E → F
    // chain: E → F → X → C → D → X
    this->chain.splice(start, this->chain, afterLastOc, tail);

    // Move the old prefix from the temporary list to the end of the original list
    // temp: A → B
    // chain: E → F → X → C → D → X → A → B
    // temp:  (empty)
    this->chain.splice(tail, temp);
}
