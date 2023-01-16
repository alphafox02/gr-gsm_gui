import tkinter as tk
from tkinter import ttk 
import subprocess

window = tk.Tk()

# Function to get list of devices
def get_devices():
    device_list = subprocess.run(["grgsm_scanner", "-l"], capture_output=True).stdout.decode('utf-8').split('\n')
    # Split each line on the comma and take the first item in the list
    device_list = [line.split(',')[0] for line in device_list]
    device_list.pop()
    return device_list

# Function to start GSM Scan
def gsm_scan(device, band):
    # start scan
    subprocess.Popen(["gnome-terminal", "-e", "bash -c \"grgsm_scanner --args {} -b {} -v -d; bash\"".format(device, band)])

# Function to start livemon
def livemon(device, freq):
    # start livemon
    subprocess.Popen(["gnome-terminal", "-e", "bash -c \"grgsm_livemon --args {} -f {}; bash\"".format(device, freq)])

# Function to start IMSI Catcher
def imsi_catcher():
    # start imsi catcher
    subprocess.Popen(["gnome-terminal", "-e", "bash -c \"cd /usr/src/IMSI-catcher/; sudo python3 simple_IMSI-catcher.py --sniff; bash\""])

# Main Window
window.title("GSM Monitoring GUI")
window.geometry("500x400")

# Dropdown for GSM Devices
tk.Label(window, text="Select GSM Device:").grid(row=0, column=0, padx=10, pady=10)
device_list = get_devices()
device_variable = tk.StringVar(window)
device_variable.set("Select Device")
device_dropdown = ttk.Combobox(window, textvariable=device_variable, values=device_list)
device_dropdown.grid(row=0, column=1, padx=10, pady=10)

# Dropdown for GSM Bands
tk.Label(window, text="Select GSM Band:").grid(row=1, column=0, padx=10, pady=10)
band_list = ["GSM900", "DCS1800", "GSM850", "PCS1900", "GSM450", "GSM480", "GSM-R"]
band_variable = tk.StringVar(window)
band_variable.set("Select Band")
band_dropdown = ttk.Combobox(window, textvariable=band_variable, values=band_list)
band_dropdown.grid(row=1, column=1, padx=10, pady=10)

# Textbox for Frequency
tk.Label(window, text="Enter Frequency:").grid(row=2, column=0, padx=10, pady=10)
freq = tk.StringVar()
freq_entry = tk.Entry(window, textvariable=freq)
freq_entry.grid(row=2, column=1, padx=10, pady=10)

# Buttons
gsm_scan_button = tk.Button(window, text="Scan", command=lambda: gsm_scan(device_variable.get(), band_variable.get()))
gsm_scan_button.grid(row=3, column=0, padx=10, pady=10)

livemon_button = tk.Button(window, text="Livemon", command=lambda: livemon(device_variable.get(), freq.get()))
livemon_button.grid(row=3, column=1, padx=10, pady=10)

imsi_catcher_button = tk.Button(window, text="IMSI Catcher", command=lambda: imsi_catcher())
imsi_catcher_button.grid(row=3, column=2, padx=10, pady=10)

window.mainloop()
