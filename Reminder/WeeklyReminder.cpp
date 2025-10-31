#include "WeeklyReminder.h" // 📦 Include class header for implementation
#include <iostream>
#include <vector>

// =========================
// 🏗️ Default constructor
// =========================
WeeklyReminder::WeeklyReminder() {
    this->days = new int(7);               // 🔢 Allocate memory for number of days (7)
    this->arr = new DailyReminder*[7];     // 📦 Allocate array of 7 pointers to DailyReminder

    // 🧱 Initialize each pointer with a new DailyReminder
    for (int i = 0; i < *this->days; i++) {
        this->arr[i] = new DailyReminder();  // 🧩 Each day gets its own DailyReminder object
    }
}

// =========================
// 🧹 Destructor
// =========================
WeeklyReminder::~WeeklyReminder() {
    // 🚨 If the array exists, delete all the DailyReminder objects inside
    if (this->arr) {
        for (int i = 0; i < (this->days ? *this->days : 0); i++) {
            delete this->arr[i];  // 🧹 Delete each DailyReminder pointer
        }
        delete[] this->arr;       // 🧱 Delete the array itself
    }

    delete this->days;            // 🧹 Free the memory for the days count
    this->arr = nullptr;          // 🚫 Nullify the array pointer
    this->days = nullptr;         // 🚫 Nullify the days pointer
}

// =========================
// 📋 Copy constructor
// =========================
WeeklyReminder::WeeklyReminder(const WeeklyReminder& other) {
    this->days = other.days ? new int(*other.days) : nullptr;  // 🔢 Deep copy number of days

    // 📦 Deep copy all DailyReminder pointers if array exists
    if (other.arr && this->days) {

        this->arr = new DailyReminder*[*this->days];           // 🧱 Allocate new array

        for (int i = 0; i < *this->days; i++) {
            // 🧬 Deep copy each DailyReminder using its copy constructor
            this->arr[i] = other.arr[i] ? new DailyReminder(*other.arr[i]) : nullptr;
        }
    } else {
        this->arr = nullptr; // 🚫 If no array in source, set to null
    }
}

// =========================
// 🧳 Move constructor
// =========================
WeeklyReminder::WeeklyReminder(WeeklyReminder&& other) noexcept {
    this->arr = other.arr;   // 🧳 Steal pointer to array
    this->days = other.days; // 🧳 Steal pointer to number of days

    other.arr = nullptr;     // 🚫 Nullify source pointer
    other.days = nullptr;    // 🚫 Nullify source pointer
}

// =========================
// 📋 Copy assignment operator
// =========================
WeeklyReminder& WeeklyReminder::operator=(const WeeklyReminder& other) {
    // 🚫 Avoid self-assignment
    if (this != &other) {
        // 🧹 Clean up old data
        if (this->arr) {
            // 🧹 Delete each DailyReminder object
            for (int i = 0; i < (this->days ? *this->days : 0); i++) { delete this->arr[i]; }
            delete[] this->arr; // 🧱 Delete the array
        }
        delete this->days; // 🧹 Delete days count

        // 🔢 Copy new number of days
        this->days = other.days ? new int(*other.days) : nullptr;

        // 🧱 Deep copy DailyReminders
        if (other.arr && this->days) {
            this->arr = new DailyReminder*[*this->days];
            for (int i = 0; i < *this->days; i++) {
                this->arr[i] = other.arr[i] ? new DailyReminder(*other.arr[i]) : nullptr;
            }
        } else { this->arr = nullptr; }
    }

    return *this; // ✅ Return self-reference
}

// =========================
// 🧳 Move assignment operator
// =========================
WeeklyReminder& WeeklyReminder::operator=(WeeklyReminder&& other) noexcept {
    // 🚫 Avoid self assignment (self-move)
    if (this != &other) {
        // 🧹 Delete old data
        if (this->arr) {
            // free all DailyReminder Objects from the memory
            for (int i = 0; i < (this->days ? *this->days : 0); i++) { delete this->arr[i]; }
            // delete the DailyReminders array itself
            delete[] this->arr;
        }
        // delete the Number of days in the array (7)
        delete this->days;

        // 🧳 Steal pointers from the source
        this->arr = other.arr;
        this->days = other.days;

        // 🚫 Nullify the source, leave it in safe state
        other.arr = nullptr;
        other.days = nullptr;
    }

    return *this; // ✅ Return self-reference, for allowing chainning
}

// =========================
// ⚙️ Getter: getDay()
// =========================
const DailyReminder* WeeklyReminder::getDay(const int index) const {
    // 🚨 Check for valid range and non-null array
    if (!this->arr || index < 0 || index >= (this->days ? *this->days : 0)) return nullptr;

    return this->arr[index]; // 📦 Return pointer to DailyReminder at given index
}

// =========================
// ⚙️ Setter: setDay()
// =========================
void WeeklyReminder::setDay(const int index, DailyReminder* dr) const {
    // 🚨 Validate index
    if (!this->arr || index < 0 || index >= (this->days ? *this->days : 0)) return;

    delete this->arr[index];   // 🧹 Delete existing DailyReminder
    this->arr[index] = dr;     // 🧩 Assign the new DailyReminder pointer
}

// =========================
// ⚙️ Getter: getDaysCount()
// =========================
int WeeklyReminder::getDaysCount() const {
    // 🔢 Return number of days (if null, return 0)
    return this->days ? *this->days : 0;
}

// =========================
// ➕ Add a new reminder to a specific day
// =========================
void WeeklyReminder::addReminder(const string* cust, const string* tel, const string* inst,
                                 const string* date, const int* hour, const int dayReminder) const {
    // 🚨 Validation
    if (!cust || !tel || !inst || !date || !hour || dayReminder < 1 || dayReminder > 6) { return; }

    // 🧩 Get pointer to the proper DailyReminder 🗓️
    DailyReminder* targetDay = this->arr[dayReminder];

    if (!targetDay) { return; } // 🚨 Validate that the targetDay exists


    // ➕ Create a new Reminder and Add it into that day's linked list
    targetDay->addReminder(new Reminder(cust, tel, inst, date, hour));
}

// =========================
// 🔄 Update reminder status or move unanswered to day 0
// =========================
void WeeklyReminder::updateReminder(const string* cust, const string* inst,
                                    const int dayReminder, const int answer) const {
    // 🚨 Validate all input pointers and ensure day index in range 1–6
    if (!cust || !inst || dayReminder < 1 || dayReminder > 6) { return;}

    DailyReminder* targetDay = this->arr[dayReminder]; // 🗓️ Get pointer to the specific day’s DailyReminder

    if (!targetDay) { return; } // 🚨 Validate that the target day exists

    // 🧭 Traversal pointer to go through the list of reminders
    const Node<Reminder *> *pos = targetDay->getChain();
    bool found = false; // 🔍 Flag to check if a matching reminder was found

    // 🔁 Loop through all reminders of the given day
    while (pos) {
        // 🧩 Check if both customer name and institute match
        if (Reminder *r = pos->getValue(); r && *r->getCust() == *cust && *r->getInst() == *inst) {
            found = true; // ✅ Found the matching reminder

            // 🧱 Update reminder status directly
            if (answer == 1 || answer == 2) { r->setStatus(new int(answer)); }

            // ⚙️ If the answer is "0" (no response)
            else if (answer == 0) {
                targetDay->removeReminderByName(cust); // 🧭 Remove this reminder and move it to arr[0]
                this->arr[0]->addReminder(r); // 🧱 Add the same reminder (moved) to day 0 (unanswered list)
            }

            break; // 🛑 Stop searching after handling the match
        }

        // ⏩ Move to the next node in the list
        pos = pos->getNext();
    }

    // ⚠️ If no matching reminder found, print warning
    if (!found) {
        cout << "❌ Reminder not found for " << cust->c_str() << " (" << inst->c_str() << ")" << endl;
    }
}

// =========================
// 🏥 Print all cancelled appointments per unique institute (auto-detected)
// =========================
void WeeklyReminder::printCancelledAppointments() const {
    // 🚨 If no array or days pointer exists — nothing to print
    if (!this->arr || !this->days) { return; }

    // 🧾 Header
    cout << "🏥 Cancelled Appointments Report — 'Kol HaBriut'\n";

    // 🧱 Step 1: Build a list of unique institute names
    vector<string*> institutes;  // 🧩 dynamic list of unique names

    for (int day = 0; day < *this->days; day++) {
        const DailyReminder* daily = this->arr[day];  // 📅 Get daily reminder pointer

        if (!daily) continue;

        // 🧭 Traverse reminders for this day
        const Node<Reminder *> *pos = daily->getChain();

        while (pos) {
            const Reminder* r = pos->getValue();  // 🎯 Extract reminder pointer
            pos = pos->getNext();            // ⏩ Move to next node

            if (!r || !r->getInst()) continue; // 🚫 Skip null entries

            const string *name = r->getInst();  // 🏥 Pointer to institute name
            bool exists = false;

            // 🔍 Check if this institute already in our list
            for (const auto* inst : institutes) {
                if (*inst == *name) { exists = true; break; }
            }

            // ➕ If not found, add to list
            if (!exists) institutes.push_back(const_cast<vector<string *>::value_type>(name));
        }
    }

    // 🧱 Step 2: Print all cancellations per institute
    for (const auto* currentInst : institutes) {
        if (!currentInst) continue;

        cout << "🏛️ Institute: " << currentInst->c_str() << "\n";
        bool found = false;  // 🔍 Flag to track if cancellations found

        for (int day = 0; day < *this->days; day++) {
            const DailyReminder* daily = this->arr[day];
            if (!daily) continue;

            const Node<Reminder *> *pos = daily->getChain();

            while (pos) {
                const Reminder* r = pos->getValue();
                pos = pos->getNext();

                // 🚫 Skip null or incomplete reminders
                if (!r || !r->getStatus() || !r->getInst()) continue;

                // ✅ If matches current institute and was cancelled (status = 2)
                if (*r->getInst() == *currentInst && *r->getStatus() == 2) {
                    found = true;
                    cout << "   ❌ Cancelled: "
                         << "Customer: " << r->getCust()->c_str() << " | "
                         << "Date: " << r->getDate()->c_str() << " | "
                         << "Hour: " << *r->getHour() << "\n";
                }
            }
        }

        // ⚠️ If no cancellations found
        if (!found) {
            cout << "   ✅ No cancelled appointments for this institute.\n";
        }

        cout << "-----------------------------------------------\n";
    }

    // 🧹 No need to delete institutes — we only stored pointers to existing strings
}

// =========================
// 🧾 Utility: print()
// =========================
void WeeklyReminder::print() const {
    cout << "🗓️ Weekly Reminder System ("
         << (this->days ? *this->days : 0) << " days total)" << endl; // 🧾 Header

    // 🔁 Loop over each day
    for (int i = 0; i < (this->days ? *this->days : 0); i++) {
        cout << "📅 Day " << i << ":" << endl; // 🧩 Print day number

        // 🧱 Print daily reminders if exist
        if (this->arr && this->arr[i]) {
            this->arr[i]->print(); // 🖨️ Print contents of DailyReminder
        } else {
            cout << "❌ No reminders for this day.\n"; // ⚠️ Indicate empty slot
        }

        cout << "---------------------------------\n"; // 🔻 Visual separator
    }
}
