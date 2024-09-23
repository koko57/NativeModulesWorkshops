import Charts

let enableRotationNotification = "ChartsEnableRotation"

@objc(ChartsViewManager)
class ChartsViewManager: RCTViewManager {
  @objc func selectValue(_ reactTag: NSNumber, datasetIndex: NSNumber, x: NSNumber) {
      guard let bridge = self.bridge else {
        print("Bridge is not set")
        return
      }

      let uiManager = bridge.module(for: RCTUIManager.self) as! RCTUIManager
      uiManager.addUIBlock { (uiManager, viewRegistry: [NSNumber: UIView]?) in
        guard let viewRegistry = viewRegistry else {
          print("View registry is nil")
          return
        }
        
        if let view = viewRegistry[reactTag] as? ChartsView {
          view.selectValue(datasetIndex: datasetIndex.intValue, x: x.doubleValue)
        } else {
          print("Invalid view returned from registry, expecting ChartsView")
        }
      }
    }
  
  override func view() -> (ChartsView) {
    return ChartsView()
  }

  @objc override static func requiresMainQueueSetup() -> Bool {
    return false
  }
}

class ChartsView : RadarChartView {

  init() {
    super.init(frame: CGRect(x: 0, y: 0, width: 0, height: 0))
    NotificationCenter.default.addObserver(self, selector: #selector(enableRotation), name: Notification.Name(enableRotationNotification), object: nil)
    self.delegate = self
  }

  required init?(coder: NSCoder) {
    super.init(coder: coder)
  }
  
  
  override func removeFromSuperview() {
    super.removeFromSuperview()
    NotificationCenter.default.removeObserver(self)
  }

  @objc func enableRotation(_ notification: Notification){
    if let enable = notification.object as? Bool {
      rotationEnabled = enable
    }
  }

  @objc var onValueSelected: RCTBubblingEventBlock?

  @objc var RNWebColor: String = "" {
    didSet {
      webColor = hexStringToUIColor(hexColor: RNWebColor)
      setNeedsDisplay()
    }
  }

  @objc var RNInnerWebColor: String = "" {
    didSet {
      innerWebColor = hexStringToUIColor(hexColor: RNInnerWebColor)
      setNeedsDisplay()
    }
  }

  @objc var RNLegendEnabled: NSNumber = 0 {
    didSet {
      if(RNLegendEnabled.boolValue){
        legend.enabled = true
      } else {
        legend.enabled = false
      }
      setNeedsDisplay()
    }
  }

  @objc var RNWebLineWidth: NSNumber = 1 {
    didSet {
      webLineWidth = CGFloat(RNWebLineWidth.floatValue)
      setNeedsDisplay()
    }
  }

  @objc var RNInnerWebLineWidth: NSNumber = 1 {
    didSet {
      innerWebLineWidth = CGFloat(RNInnerWebLineWidth.floatValue)
      setNeedsDisplay()
    }
  }

  @objc var datasets: NSArray = [] {
    didSet {
      data = mapDatasets(datasets: datasets)
    }
  }

  func mapDatasets(datasets: NSArray) -> RadarChartData? {
    if let datasets = datasets as? [NSDictionary] {
      let chartDataSets = datasets.map { (record) -> RadarChartDataSet? in
        mapDatasetRecord(record: record);
      }
      if let chartDataSets = chartDataSets as? [RadarChartDataSet] {
        return RadarChartData(dataSets: chartDataSets)
      }
    }
    return nil
  }

  func selectValue(datasetIndex: Int, x: Double) -> Void {
    let highlight = Highlight(x: x, dataSetIndex: datasetIndex, stackIndex: 0)
    highlightValue(highlight)
  }

  func mapDatasetRecord(record: NSDictionary) -> RadarChartDataSet? {
    let name = record["name"]
    let lineColor = record["lineColor"]
    let fillColor = record["fillColor"]
    let lineWidth = record["lineWidth"]
    let drawFilledEnabled = record["drawFilledEnabled"]

    let data = record["data"]
    if let data = data as? [Int] {
      let entries = data.map { (int) -> RadarChartDataEntry in
        RadarChartDataEntry(value: Double(int))
      }
      let chartDataSet = RadarChartDataSet(entries: entries, label: name as! String)
      if let lineColor = lineColor as? String {
        chartDataSet.colors = [hexStringToUIColor(hexColor: lineColor)]
      }
      if let fillColor = fillColor as? String {
        chartDataSet.fillColor = hexStringToUIColor(hexColor: fillColor)
      }
      if let lineWidth = lineWidth as? Double {
        chartDataSet.lineWidth = lineWidth
      }
      if let drawFilledEnabled = drawFilledEnabled as? Bool {
        chartDataSet.drawFilledEnabled = drawFilledEnabled
      }
      return chartDataSet
    }
    return nil
  }

  func hexStringToUIColor(hexColor: String) -> UIColor {
    let stringScanner = Scanner(string: hexColor)

    if(hexColor.hasPrefix("#")) {
      stringScanner.scanLocation = 1
    }
    var color: UInt32 = 0
    stringScanner.scanHexInt32(&color)

    let r = CGFloat(Int(color >> 16) & 0x000000FF)
    let g = CGFloat(Int(color >> 8) & 0x000000FF)
    let b = CGFloat(Int(color) & 0x000000FF)

    return UIColor(red: r / 255.0, green: g / 255.0, blue: b / 255.0, alpha: 1)
  }
}

extension ChartsView: ChartViewDelegate {
  func chartValueSelected(_ chartView: ChartViewBase, entry: ChartDataEntry, highlight: Highlight) {
    guard let onValueSelected = self.onValueSelected else { return }
    let params: [String : Any] = [
      "datasetIndex": highlight.dataSetIndex,
      "x": highlight.x,
      "y": highlight.y
    ]
    onValueSelected(params);
  }
}
