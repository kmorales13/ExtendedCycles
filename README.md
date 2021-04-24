# 🌅 Extended Cycles [Fabric]

## About

*Ever wanted to make your minecraft days/nights last longer?*

ExtendedCycles mod allows you to extend the duration of the day or night.

This is still a work-in-progress and may not work as expected.

Based on the work of SilentChaos512.

## Concept

Time in minecraft is complex, and trying to modify it even more.

ExtendedCycles works by adding a period of frozen time. This means it basically stops the internal clock for a given amount of time.

**This may cause some other mods that rely on this internal clock to break or not work correctly.**

## Usage

Just add the jar file to your fabric instance. 

This mod is server-side only. Vanilla clients may connect to a server using this mod without problem.

After the first run, the mod will create a config file 'extended_cycles.json' in the 'config' directory.

## Config

The config file will have two options for day and night.

1 second = 20 ticks

- Entry: exact tick when the frozen time will begin (0 - 24000)
- Minutes: amount of time to freeze time in minutes

Using a negative value for the minutes value will *skip* time ahead.
