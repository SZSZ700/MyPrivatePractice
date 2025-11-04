cmake_minimum_required(VERSION 4.0)
project(untitled1)
add_compile_options(-fexec-charset=UTF-8 -finput-charset=UTF-8)

set(CMAKE_CXX_STANDARD 20)

add_executable(untitled1 Main/main.cpp
        Node/Node.h
        BinNode/BinNode.h
        Buffer/Buffer.cpp
        Buffer/BufferAuto.cpp
        Buffer/Buffer.h
        Buffer/BufferAuto.h
        Queue/Queue.h
        Stack/Stack.h
        DoublyLinkedList/List.h
        Queue/PriorityQueue.h
        "ForwardLinkedList/ForwardList .h"
        "overloadOperators/Complex .cpp"
        "overloadOperators/Complex .h"
        "Memory Pool/Box.cpp"
        "Memory Pool/Box.h"
        Driver/Driver.h
        Driver/Driver.cpp
        Driver/DriversDb.h
        Driver/DriversDb.cpp
        Driver/DepartmentofTransportation.h
        Driver/DepartmentOfTransportation.cpp
        SetOfNumbers/SetOfNumbers.cpp
        SetOfNumbers/SetOfNumbers.h
        Reminder/Reminder.cpp
        Reminder/Reminder.h
        Reminder/DailyReminder.cpp
        Reminder/DailyReminder.h
        Reminder/WeeklyReminder.cpp
        Reminder/WeeklyReminder.h
        Seven/Data.cpp
        Seven/Data.h
        Seven/Memory.cpp
        Seven/Memory.h
        Employees/Employee.cpp
        Employees/Employee.h
        "Employees/Nurse.cpp"
        "Employees/Nurse.h"
        Employees/Doctor.cpp
        Employees/Doctor.h
        Employees/Supervisor.cpp
        Employees/Supervisor.h
        Employees/AllEmployees.cpp
        Employees/AllEmployees.h
)
