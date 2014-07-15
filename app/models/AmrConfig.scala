package models

case class AmrConfig(
  oaVersion: String,
  unsolicitedEvents: String,
  outageResponse: String,
  manualResponse: String,
  validateDups: Boolean,
  validateEventDate: Boolean,
  disableTagLevel: Int,
  suspendedLoadCIS: Boolean,
  outageEventEnable: Boolean,
  restoreEventEnable: Boolean,
  repingOnOutage: Int,
  repingDelay: Int,
  repingWindow: Int)
