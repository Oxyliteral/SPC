# Starsector, Preferable Changes
A mod for Starsector, which changes the in-game options and skills. Has both text file and LunaLib compatibility settings, where skills or in-game options can be disabled/enabled.

Calls resetCached() function of SettingsAPI, which updates runtime values like max level and etc, but does not edit game engine values, like base speed or max fleet size. Currently unsure how to fix this.
