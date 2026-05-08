import tkinter as tk
import subprocess

#map made by Claude
HELP_MAP = {
    "Dolphin":            {"java": "Files",        "src": "/opt/silveros/help/Files.java"},
    "Google Chrome":      {"java": "GoogleChrome", "src": "/opt/silveros/help/GoogleChrome.java"},
    "Audible":            {"java": "Audible",      "src": "/opt/silveros/help/Audible.java"},
    "Medisafe":           {"java": "Medisafe",     "src": "/opt/silveros/help/Medisafe.java"},
    "MyChart":            {"java": "MyChart",      "src": "/opt/silveros/help/MyChart.java"},
    "Gmail":              {"java": "Email",        "src": "/opt/silveros/help/Email.java"},
    "Google Calendar":    {"java": "CalendarHelp", "src": "/opt/silveros/help/CalendarHelp.java"},
    "Voice Controls":     {"java": "Voice",        "src": "/opt/silveros/help/Voice.java"},
    "WhatsApp":           {"java": "WhatsApp",     "src": "/opt/silveros/help/WhatsApp.java"},
    "Words With Friends": {"java": "WWF",          "src": "/opt/silveros/help/WWF.java"},
}

def get_active_window():
    try:
        return subprocess.check_output(["xdotool", "getwindowfocus", "getwindowname"],
                                       text=True).strip()
    except:
        return ""

def launch_help():
    title = get_active_window()
    matched = None
    for key, val in HELP_MAP.items():
        if key in title:
            matched = val
            break
    if not matched:
        matched = {"java": "General", "src": "/opt/silveros/help/General.java"}

    src = matched["src"]
    cls = matched["java"]
    d = src.rsplit("/", 1)[0]
    subprocess.Popen(["bash", "-c", f"cd {d} && javac {src} && java -cp {d} {cls}"])

root = tk.Tk()
root.overrideredirect(True)     
root.attributes("-topmost", True)
root.geometry("24x24+1160+0")   
root.configure(bg="#eeeeee")

btn = tk.Button(root, text="?", font=("Arial", 20, "bold"), fg="black",
                bg="#eeeeee", relief="flat", command=launch_help)
btn.pack(expand=True, fill="both")

root.mainloop()
