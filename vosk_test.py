import vosk
import sounddevice as sd
import queue
import json
import sys
import subprocess
import os
import time
import webbrowser
import datetime

model_path = "vosk-model-small-en-us-0.15"
q = queue.Queue()

def speak(text):
        try:
            subprocess.run(["espeak", text], capture_output=True)
        except FileNotFoundError:
            pass

def input_analysis(result):
    content = result["text"].lower().strip()
    words = content.split()

    if any(w in content for w in ["shutdown", "power off", "turn off"]):
        speak("Shutting down")
        subprocess.run(["systemctl", "poweroff"])

    elif any(w in content for w in ["reboot", "restart"]):
        speak("Rebooting")
        subprocess.run(["systemctl", "reboot"])

    elif "sleep" in content or "suspend" in content:
        speak("Suspending")
        subprocess.run(["systemctl", "suspend"])

    elif "lock" in content and "screen" in content:
        speak("Locking screen")
        subprocess.run(["loginctl", "lock-session"])

    elif "volume up" in content or "increase volume" in content:
        subprocess.run(["amixer", "-q", "sset", "Master", "10%+"])
        speak("Volume up")

    elif "volume down" in content or "decrease volume" in content:
        subprocess.run(["amixer", "-q", "sset", "Master", "10%-"])
        speak("Volume down")

    elif "mute" in content:
        subprocess.run(["amixer", "-q", "sset", "Master", "toggle"])
        speak("Muted")

    elif "unmute" in content:
        subprocess.run(["amixer", "-q", "sset", "Master", "unmute"])
        speak("Unmuted")

    # elif "open" in content:
    #     app_map = {
    #         "browser":   "xdg-open https://www.google.com",
    #         "firefox":   "firefox",
    #         "chrome":    "google-chrome",
    #         "terminal":  "x-terminal-emulator",
    #         "files":     "nautilus",
    #         "calendar":  "gnome-calendar",
    #         "calculator":"gnome-calculator",
    #         "text editor":"gedit",
    #         "settings":  "gnome-control-center",
    #         "music":     "rhythmbox",
    #     }
    #     args = get_arg("open")
    #     app_phrase = " ".join(args)
    #     matched = next((cmd for key, cmd in app_map.items() if key in app_phrase), None)
    #     if matched:
    #         speak(f"Opening {app_phrase}")
    #         subprocess.Popen(matched.split())
    #     else:
    #         speak(f"I don't know how to open {app_phrase}")
    #         print(f"Unknown app: {app_phrase}")

    # elif "close" in content:
    #     args = get_arg("close")
    #     if args:
    #         app = args[0]
    #         subprocess.run(["pkill", "-f", app])
    #         speak(f"Closing {app}")
    #     else:
    #         speak("Which app should I close?")

    # elif "search" in content or "look up" in content:
    #     trigger = "search" if "search" in words else "up"
    #     query_words = get_arg(trigger)
    #     if query_words:
    #         query = "+".join(query_words)
    #         url = f"https://www.google.com/search?q={query}"
    #         webbrowser.open(url)
    #         speak(f"Searching for {' '.join(query_words)}")
    #     else:
    #         speak("What should I search for?")

    elif "open website" in content or "go to" in content:
        trigger = "to" if "to" in words else "website"
        site_args = get_arg(trigger)
        if site_args:
            site = site_args[0]
            if not site.startswith("http"):
                site = f"https://{site}"
            webbrowser.open(site)
            speak(f"Opening {site}")

    elif "what time" in content or "current time" in content:
        now = datetime.datetime.now().strftime("%I:%M %p")
        speak(f"The time is {now}")
        print(f"Time: {now}")

    elif "what date" in content or "today's date" in content or "what day" in content:
        today = datetime.datetime.now().strftime("%A, %B %d %Y")
        speak(f"Today is {today}")
        print(f"Date: {today}")
    elif "take screenshot" in content or "screenshot" in content:
        filename = os.path.expanduser(f"~/screenshot_{int(time.time())}.png")
        result = subprocess.run(["import", "-window", "root", filename], capture_output=True)
        if result.returncode == 0:
            speak("Screenshot taken")
            print(f"Saved: {filename}")
        else:
            subprocess.run(["scrot", filename], capture_output=True)
            speak("Screenshot taken")

    elif "remind me" in content:
        reminder_args = get_arg("me")
        if reminder_args:
            note = " ".join(reminder_args)
            notes_path = os.path.expanduser("~/voice_reminders.txt")
            timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M")
            with open(notes_path, "a") as f:
                f.write(f"[{timestamp}] {note}\n")
            speak(f"Reminder saved: {note}")
            print(f"Reminder saved: {note}")

    elif "show reminders" in content or "read reminders" in content:
        notes_path = os.path.expanduser("~/voice_reminders.txt")
        if os.path.exists(notes_path):
            with open(notes_path) as f:
                content_text = f.read()
            print(content_text)
            speak("Showing your reminders")
        else:
            speak("No reminders found")

    elif "copy" in content and "clipboard" in content:
        args = get_arg("copy")
        if args:
            text = " ".join(args)
            subprocess.run(["xclip", "-selection", "clipboard"], input=text.encode())
            speak("Copied to clipboard")

    elif "clear clipboard" in content:
        subprocess.run(["xclip", "-selection", "clipboard"], input=b"")
        speak("Clipboard cleared")

    elif "brightness up" in content or "increase brightness" in content:
        subprocess.run(["brightnessctl", "set", "10%+"], capture_output=True)
        speak("Brightness increased")

    elif "brightness down" in content or "decrease brightness" in content:
        subprocess.run(["brightnessctl", "set", "10%-"], capture_output=True)
        speak("Brightness decreased")

    elif "wifi on" in content or "enable wifi" in content:
        subprocess.run(["nmcli", "radio", "wifi", "on"])
        speak("Wi-Fi enabled")
        print(result_proc.stdout)
        speak("Showing Wi-Fi status")

    elif "play" in content or "pause" in content or "next track" in content or "previous track" in content:
        key_map = {
            "play":           "XF86AudioPlay",
            "pause":          "XF86AudioPause",
            "next track":     "XF86AudioNext",
            "previous track": "XF86AudioPrev",
        }
        for phrase, key in key_map.items():
            if phrase in content:
                subprocess.run(["xdotool", "key", key], capture_output=True)
                speak(phrase)
                break

    # ── Help ──────────────────────────────────────────────────
    elif "what can you do" in content or "help" in content or "commands" in content:
        commands = [
            "shutdown / reboot / sleep / lock screen",
            "volume up / down / mute / unmute",
            "open <app> / close <app>",
            "create file / delete file / list files",
            "search <query> / go to <website>",
            "what time / what date",
            "take screenshot",
            "remind me <note> / show reminders",
            "brightness up / down",
            "wifi on / off / show wifi",
            "play / pause / next track / previous track",
        ]
        print("Available commands:\n" + "\n".join(f"  • {c}" for c in commands))
        speak("Showing available commands")

    else:
        print(f"Unrecognised: '{content}'")
        speak("Sorry, I didn't understand that")

def callback(indata, frames, time, status):
	q.put(bytes(indata))

model = vosk.Model(model_path)
rec = vosk.KaldiRecognizer(model, 16000)

print("Init")

with sd.RawInputStream(samplerate=16000, blocksize=8000, dtype='int16', channels=1, callback=callback, device=1):
	for _ in range(10):
		data = q.get()
		if rec.AcceptWaveform(data):
			result = json.loads(rec.Result())
			if result.get("text"):
				input_analysis(result)

final = json.loads(rec.FinalResult())

if final.get("text"):
	input_analysis(final)
