(ns osx.core
  (:require [coffi.mem :as mem :refer [defalias]]
            [coffi.ffi :as ffi :refer [defcfn]]))

(defcfn dlopen
  dlopen
  [::mem/c-string ::mem/int] ::mem/void)

(defcfn objc_getClass
  objc_getClass
  [::mem/c-string] ::mem/pointer)

(defcfn sel_registerName
  sel_registerName
  [::mem/c-string] ::mem/pointer)

(defcfn objc_msgSend
  objc_msgSend
  [::mem/pointer ::mem/pointer] ::mem/pointer)

(dlopen "/System/Library/Frameworks/CoreWLAN.framework/Versions/Current/CoreWLAN" 0)

(def CWWiFiClient (objc_getClass "CWWiFiClient"))

(def sel_sharedWiFiClient (sel_registerName "sharedWiFiClient"))
(def sel_interface (sel_registerName "interface"))
(def sel_interfaceName (sel_registerName "interfaceName"))

(def wifi-client (objc_msgSend CWWiFiClient sel_sharedWiFiClient))

(def default-interface (objc_msgSend wifi-client sel_interface))

(def bsd-interface-name (objc_msgSend default-interface sel_interfaceName))

(println "wifi interface name:" (mem/deserialize (objc_msgSend bsd-interface-name (sel_registerName "UTF8String"))
                                                 ::mem/c-string))

(println "rssi:" ((ffi/cfn "objc_msgSend" [::mem/pointer ::mem/pointer] ::mem/long) default-interface (sel_registerName "rssiValue")))
