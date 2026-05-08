import vosk
import sounddevice as sd
import queue
import json
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
    subprocess.Popen(["zenity", "--info", "--text", text, "--title", "Voice Assistant", "--width", "400"])


def get_arg(trigger_word, content):
    words = content.split()
    if trigger_word not in words:
        return []
    idx = words.index(trigger_word)
    return words[idx + 1:]

def input_analysis(result):
    content = result["text"].lower().strip()
    words = content.split()

    if any(w in content for w in ["shut down", "power off", "turn off", "fade"]):
        subprocess.run(["systemctl", "poweroff"])

    elif any(w in content for w in ["reboot", "restart"]):
        subprocess.run(["systemctl", "reboot"])

    elif "sleep" in content or "suspend" in content:
        subprocess.run(["systemctl", "suspend"])

    elif "lock" in content and "screen" in content:

        # loginctl lock-sessions (plural) is the correct Debian/systemd command
        subprocess.run(["loginctl", "lock-sessions"])

    elif "volume up" in content or "increase volume" in content:
        # wpctl works on PipeWire/PulseAudio; fallback to pactl if needed
        result_vol = subprocess.run(
            ["wpctl", "set-volume", "@DEFAULT_AUDIO_SINK@", "10%+"],
            capture_output=True
        )
        if result_vol.returncode != 0:
            subprocess.run(["pactl", "set-sink-volume", "@DEFAULT_SINK@", "+10%"])

    elif "volume down" in content or "decrease volume" in content:
        result_vol = subprocess.run(
            ["wpctl", "set-volume", "@DEFAULT_AUDIO_SINK@", "10%-"],
            capture_output=True
        )
        if result_vol.returncode != 0:
            subprocess.run(["pactl", "set-sink-volume", "@DEFAULT_SINK@", "-10%"])

    elif "unmute" in content:
        result_vol = subprocess.run(
            ["wpctl", "set-mute", "@DEFAULT_AUDIO_SINK@", "0"],
            capture_output=True
        )
        if result_vol.returncode != 0:
            subprocess.run(["pactl", "set-sink-mute", "@DEFAULT_SINK@", "0"])

    elif "mute" in content:
        result_vol = subprocess.run(
            ["wpctl", "set-mute", "@DEFAULT_AUDIO_SINK@", "toggle"],
            capture_output=True
        )
        if result_vol.returncode != 0:
            subprocess.run(["pactl", "set-sink-mute", "@DEFAULT_SINK@", "toggle"])

    elif "open" in content:
        app_map = {
            "browser":          ["google-chrome"],
            "chrome":           ["google-chrome"],
            "internet":         ["google-chrome"],
            "incognito":        ["/usr/bin/google-chrome-stable", "--incognito"],
            "terminal":         ["x-terminal-emulator"],
            "files":            ["dolphin"],
            "file manager":     ["dolphin"],
            "calendar":         ["google-chrome", "--app=https://calendar.google.com"],
            "calculator":       ["kcalc"],
            "text editor":      ["kate"],
            "settings":         ["systemsettings5"],
            "audible":          ["google-chrome", "--app=https://www.audible.com/", "--start-maximized"],
            "email":            ["google-chrome", "--app=https://mail.google.com"],
            "mail":             ["google-chrome", "--app=https://mail.google.com"],
            "medisafe":         ["google-chrome", "--app=https://www.medisafe.com"],
            "mychart":          ["google-chrome", "--app=https://www.mychart.org/"],
            "whatsapp":         ["google-chrome", "--app=https://www.whatsapp.com/"],
            "words with friends": ["google-chrome", "--app=https://www.wordswithfriends.com"],
            "zoom":             ["/usr/bin/zoom"],
        }
        args = get_arg("open", content)
        app_phrase = " ".join(args)
        matched = next((cmd for key, cmd in app_map.items() if key in app_phrase), None)
        if matched:
            subprocess.Popen(matched)
        else:
            print(f"Unknown app: {app_phrase}")

    elif "close" in content:
        args = get_arg("close", content)
        if args:
            app = args[0]
            subprocess.run(["pkill", "-f", app])

    elif "search" in content or "look up" in content:
        trigger = "search" if "search" in words else "up"
        query_words = get_arg(trigger, content)
        if query_words:
            query = "+".join(query_words)
            url = f"https://www.google.com/search?q={query}"
            webbrowser.open(url)

    elif "what time" in content or "current time" in content:
        now = datetime.datetime.now().strftime("%I:%M %p")
        speak(f"The time is {now}")

    elif "what date" in content or "today's date" in content or "what day" in content:
        today = datetime.datetime.now().strftime("%A, %B %d %Y")
        speak(f"Today is {today}")

    elif "take screenshot" in content or "screenshot" in content:
        filename = os.path.expanduser(f"~/screenshot_{int(time.time())}.png")
        # spectacle is the KDE screenshot tool; fallback to scrot
        result_ss = subprocess.run(
            ["spectacle", "--fullscreen", "--background", "--nonotify", "--output", filename],
            capture_output=True
        )
        if result_ss.returncode != 0:
            result_ss = subprocess.run(["scrot", filename], capture_output=True)
        if result_ss.returncode == 0:
            speak(f"Saved: {filename}")
        else:
            speak("Screenshot failed")

    elif "remind me" in content:
        reminder_args = get_arg("me", content)
        if reminder_args:
            note = " ".join(reminder_args)
            notes_path = os.path.expanduser("~/voice_reminders.txt")
            timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M")
            with open(notes_path, "a") as f:
                f.write(f"[{timestamp}] {note}\n")
            speak(f"Reminder saved: {note}")
        else:
            speak("What should I remind you about?")

    elif "show reminders" in content or "read reminders" in content:
        notes_path = os.path.expanduser("~/voice_reminders.txt")
        if os.path.exists(notes_path):
            with open(notes_path) as f:
                content_text = f.read()
            speak(content_text)
        else:
            speak("No reminders found")

    elif "copy" in content and "clipboard" in content:
        args = get_arg("copy", content)
        if args:
            text = " ".join(args)
            # xclip preferred; fallback to xsel (both available on Debian)
            result_clip = subprocess.run(
                ["xclip", "-selection", "clipboard"],
                input=text.encode(),
                capture_output=True
            )
            if result_clip.returncode != 0:
                subprocess.run(
                    ["xsel", "--clipboard", "--input"],
                    input=text.encode()
                )

    elif "clear clipboard" in content:
        result_clip = subprocess.run(
            ["xclip", "-selection", "clipboard"],
            input=b"",
            capture_output=True
        )
        if result_clip.returncode != 0:
            subprocess.run(["xsel", "--clipboard", "--input"], input=b"")

    elif "brightness up" in content or "increase brightness" in content:
        result_b = subprocess.run(
            ["brightnessctl", "set", "10%+"],
            capture_output=True
        )
        if result_b.returncode != 0:
            # xrandr fallback — adjusts connected displays
            subprocess.run(
                ["xrandr", "--output", "eDP-1", "--brightness", "0.9"],
                capture_output=True
            )

    elif "brightness down" in content or "decrease brightness" in content:
        result_b = subprocess.run(
            ["brightnessctl", "set", "10%-"],
            capture_output=True
        )
        if result_b.returncode != 0:
            subprocess.run(
                ["xrandr", "--output", "eDP-1", "--brightness", "0.7"],
                capture_output=True
            )

    elif "wifi off" in content or "disable wifi" in content or "why fight off" in content or "disable why fight" in content:
        subprocess.run(["nmcli", "radio", "wifi", "off"])


    elif "show wifi" in content or "wifi status" in content or "show why fight" in content or "why fight status" in content:
        result_proc = subprocess.run(
            ["nmcli", "radio", "wifi"],
            capture_output=True, text=True
        )
        speak(result_proc.stdout)

    elif "wifi on" in content or "enable wifi" in content or "why fight on" in content or "enable why fight" in content:
        subprocess.run(["nmcli", "radio", "wifi", "on"])


    elif "play" in content or "pause" in content or "next track" in content or "previous track" in content:
        # playerctl works with any MPRIS-compatible player (Elisa, VLC, Firefox, etc.)
        if "next track" in content:
            subprocess.run(["playerctl", "next"], capture_output=True)

        elif "previous track" in content:
            subprocess.run(["playerctl", "previous"], capture_output=True)

        elif "pause" in content:
            subprocess.run(["playerctl", "pause"], capture_output=True)

        elif "play" in content:
            subprocess.run(["playerctl", "play"], capture_output=True)

    elif "what can you do" in content or "help" in content or "commands" in content:
        commands = [
            "shutdown / reboot / sleep / lock screen",
            "volume up / down / mute / unmute",
            "open <app> / close <app>",
            "search <query> / go to <website>",
            "what time / what date",
            "take screenshot",
            "remind me <note> / show reminders",
            "brightness up / down",
            "wifi on / off / show wifi",
            "play / pause / next track / previous track",
        ]
        print("Available commands:\n" + "\n".join(f"  • {c}" for c in commands))
        subprocess.Popen(["bash", "-c", "cd /opt/silveros/help && javac Voice.java && java -cp /opt/silveros/help Voice &"])

    else:
        print(f"Unrecognised: '{content}'")


def callback(indata, frames, time_info, status):
    q.put(bytes(indata))

model = vosk.Model(model_path)
rec = vosk.KaldiRecognizer(model, 16000)

with sd.RawInputStream(
    samplerate=16000,
    blocksize=8000,
    dtype="int16",
    channels=1,
    callback=callback,
    device=1
):
    while True:
        data = q.get()
        if rec.AcceptWaveform(data):
            result = json.loads(rec.Result())
            if result.get("text"):
                print(f"Heard: {result['text']}")
                input_analysis(result)


