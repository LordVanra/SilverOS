import vosk
import sounddevice as sd
import queue
import json
import sys
import subprocess

model_path = "vosk-model-small-en-us-0.15"
q = queue.Queue()

def input_analysis(result):
	content = result["text"]

	if "open" in content:
		print("Opening app")

	elif "close" in content:
		print("Closing app")

	elif "shutdown" in content:
		subprocess.run(["systemctl", "reboot"])

	else:
		print("Misunderstood: " + content)

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
