Imports System, System.IO, System.Collections.Generic, System.Runtime.InteropServices, System.ComponentModel
Imports System.Drawing, System.Drawing.Drawing2D, System.Drawing.Imaging, System.Windows.Forms

'PLEASE LEAVE CREDITS IN SOURCE, DO NOT REDISTRIBUTE!

'--------------------- [ Theme ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /Theme ] ---------------------

'PLEASE LEAVE CREDITS IN SOURCE, DO NOT REDISTRIBUTE!


Enum MouseState As Byte
    None = 0
    Over = 1
    Down = 2
    Block = 3
End Enum


'--------------------- [ Theme ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /Theme ] ---------------------
Public Class ReactorTheme : Inherits ContainerControl

    Dim WithEvents CloseBtn As New ReactorTopButton With {.Location = New Point(412, 7), .ButtonType = ReactorTopButton.topButtonType.Close, .CloseButtonFunction = ReactorTopButton.closeFunc.CloseForm}
    Dim WithEvents MinimBtn As New ReactorTopButton With {.Location = New Point(393, 7), .ButtonType = ReactorTopButton.topButtonType.Minimize}

#Region " Control Help - Movement & Flicker Control "
    Private MouseP As Point = New Point(0, 0)
    Private Cap As Boolean = False
    Private MoveHeight As Integer
    Protected Overrides Sub OnPaintBackground(ByVal e As System.Windows.Forms.PaintEventArgs)
    End Sub
    Protected Overrides Sub OnMouseDown(ByVal e As System.Windows.Forms.MouseEventArgs)
        MyBase.OnMouseDown(e)
        If e.Button = Windows.Forms.MouseButtons.Left And New Rectangle(0, 0, Width, MoveHeight).Contains(e.Location) Then
            Cap = True : MouseP = e.Location
        End If
    End Sub
    Protected Overrides Sub OnMouseUp(ByVal e As System.Windows.Forms.MouseEventArgs)
        MyBase.OnMouseUp(e) : Cap = False
    End Sub
    Protected Overrides Sub OnMouseMove(ByVal e As System.Windows.Forms.MouseEventArgs)
        MyBase.OnMouseMove(e)
        If Cap Then
            Parent.Location = MousePosition - MouseP
        End If
    End Sub
    Protected Overrides Sub OnTextChanged(ByVal e As System.EventArgs)
        MyBase.OnTextChanged(e)
        Invalidate()
    End Sub

#End Region

    Sub New()
        MyBase.New()
        Dock = DockStyle.Fill
        MoveHeight = 45
        Font = New Font("Verdana", 6.75F)
        DoubleBuffered = True

        Controls.Add(CloseBtn)
        Controls.Add(MinimBtn)
    End Sub

    Protected Overrides Sub OnPaint(ByVal e As System.Windows.Forms.PaintEventArgs)
        Dim G As Graphics = e.Graphics
        MyBase.OnPaint(e)
        If Not Me.ParentForm.FormBorderStyle = FormBorderStyle.None Then
            Me.ParentForm.FormBorderStyle = FormBorderStyle.None
        End If

        CloseBtn.Location = New Point(Width - 23, 7)
        MinimBtn.Location = New Point(Width - 42, 7)

        Dim glossLGB As New LinearGradientBrush(New Rectangle(0, 0, Width, 15), Color.FromArgb(102, 97, 93), Color.FromArgb(55, 54, 52), 90S)
        Dim glossLGB2 As New LinearGradientBrush(New Rectangle(0, 15, Width, 15), Color.Black, Color.FromArgb(26, 25, 21), 90S)
        Dim shadowLGB As New LinearGradientBrush(New Rectangle(5, 31, Width - 11, 20), Color.FromArgb(23, 23, 22), Color.FromArgb(38, 38, 38), 90S)

        G.Clear(Color.FromArgb(26, 25, 21))
        Try
            G.FillRectangle(glossLGB, New Rectangle(0, 0, Width, 15))
            G.FillRectangle(glossLGB2, New Rectangle(0, 15, Width, 15))
            G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(42, 41, 37))), 4, 30, Width - 9, Height - 36)
            G.FillRectangle(New SolidBrush(Color.FromArgb(38, 38, 38)), New Rectangle(5, 31, Width - 11, Height - 38))
            G.FillRectangle(shadowLGB, New Rectangle(5, 31, Width - 11, 20))
            G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(24, 24, 24))), 5, 32, Width - 7, 32)
            G.DrawRectangle(Pens.Black, New Rectangle(5, 31, Width - 11, Height - 38))
            G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(151, 150, 146))), 1, 1, Width - 2, 1)

            G.DrawRectangle(Pens.Black, New Rectangle(0, 0, Width - 1, Height - 1))

            'G.DrawString(Text, Font, Brushes.Black, Width / 2 - (3 * Text.Length) + 3, 7)
            'G.DrawString(Text, Font, Brushes.White, Width / 2 - (3 * Text.Length) + 3, 8)
            G.DrawString(Text, Font, Brushes.Black, New Rectangle(0, 10, Width - 1, 10), New StringFormat With {.LineAlignment = StringAlignment.Center, .Alignment = StringAlignment.Center})
            G.DrawString(Text, Font, Brushes.White, New Rectangle(0, 11, Width - 1, 11), New StringFormat With {.LineAlignment = StringAlignment.Center, .Alignment = StringAlignment.Center})
        Catch ex As Exception
        End Try
    End Sub
End Class


'--------------------- [ Button ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /Button ] ---------------------
Public Class ReactorButton : Inherits Control

#Region " Control Help - MouseState & Flicker Control"
    Private State As MouseState = MouseState.None
    Protected Overrides Sub OnMouseEnter(ByVal e As System.EventArgs)
        MyBase.OnMouseEnter(e)
        State = MouseState.Over
        Invalidate()
    End Sub
    Protected Overrides Sub OnMouseDown(ByVal e As System.Windows.Forms.MouseEventArgs)
        MyBase.OnMouseDown(e)
        State = MouseState.Down
        Invalidate()
    End Sub
    Protected Overrides Sub OnMouseLeave(ByVal e As System.EventArgs)
        MyBase.OnMouseLeave(e)
        State = MouseState.None
        Invalidate()
    End Sub
    Protected Overrides Sub OnMouseUp(ByVal e As System.Windows.Forms.MouseEventArgs)
        MyBase.OnMouseUp(e)
        State = MouseState.Over
        Invalidate()
    End Sub
    Protected Overrides Sub OnPaintBackground(ByVal pevent As System.Windows.Forms.PaintEventArgs)
    End Sub
    Protected Overrides Sub OnTextChanged(ByVal e As System.EventArgs)
        MyBase.OnTextChanged(e)
        Invalidate()
    End Sub
#End Region

    Sub New()
        MyBase.New()
        BackColor = Color.FromArgb(38, 38, 38)
        Font = New Font("Verdana", 6.75F)
        DoubleBuffered = True
    End Sub

    Protected Overrides Sub OnPaint(ByVal e As System.Windows.Forms.PaintEventArgs)
        Dim G As Graphics = e.Graphics
        MyBase.OnPaint(e)

        G.Clear(Color.FromArgb(36, 34, 30))
        Try
            Select Case State
                Case MouseState.None 'Mouse None
                    G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(36, 34, 30))), 2, Height - 1, Width - 4, Height - 1)
                    Dim backGrad As New LinearGradientBrush(New Rectangle(1, 1, Width - 1, Height - 2), Color.FromArgb(10, 9, 8), Color.FromArgb(31, 29, 26), 90S)
                    G.FillRectangle(backGrad, New Rectangle(1, 1, Width - 1, Height - 2))
                    G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(46, 45, 44))), New Rectangle(1, 1, Width - 3, Height - 4))
                Case MouseState.Over 'Mouse Hover
                    G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(46, 44, 38))), 2, Height - 1, Width - 4, Height - 1)
                    Dim backGrad As New LinearGradientBrush(New Rectangle(1, 1, Width - 1, Height - 2), Color.FromArgb(230, 0, 0), Color.FromArgb(255, 0, 0), 90S)
                    G.FillRectangle(backGrad, New Rectangle(1, 1, Width - 1, Height - 2))
                    G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(255, 0, 0))), New Rectangle(1, 1, Width - 3, Height - 4))
                Case MouseState.Down 'Mouse Down
                    G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(36, 34, 30))), 2, Height - 1, Width - 4, Height - 1)
                    Dim backGrad As New LinearGradientBrush(New Rectangle(1, 1, Width - 1, Height - 2), Color.FromArgb(190, 0, 0), Color.FromArgb(207, 0, 0), 270S)
                    G.FillRectangle(backGrad, New Rectangle(1, 1, Width - 1, Height - 2))
                    G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(235, 0, 0))), New Rectangle(1, 1, Width - 3, Height - 4))
            End Select

            Dim glossGradient As New LinearGradientBrush(New Rectangle(1, 1, Width - 2, Height / 2 - 2), Color.FromArgb(80, Color.White), Color.FromArgb(50, Color.White), 90S)
            G.FillRectangle(glossGradient, New Rectangle(1, 1, Width - 2, Height / 2 - 2))
            G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(21, 20, 18))), New Rectangle(0, 0, Width - 1, Height - 2))

            G.DrawString(Text, Font, Brushes.Black, New Rectangle(0, -2, Width - 1, Height - 1), New StringFormat With {.LineAlignment = StringAlignment.Center, .Alignment = StringAlignment.Center})
            G.DrawString(Text, Font, Brushes.White, New Rectangle(0, -1, Width - 1, Height - 1), New StringFormat With {.LineAlignment = StringAlignment.Center, .Alignment = StringAlignment.Center})
        Catch ex As Exception
        End Try
    End Sub
End Class


'--------------------- [ TopButton ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /TopButton ] ---------------------
Public Class ReactorTopButton : Inherits Control

#Region " Control Help - MouseState & Flicker Control"
    Dim buttonColor As Color
    Enum topButtonType As Byte
        Blank = 0
        Minimize = 1
        Maximize = 2
        Close = 3
    End Enum
    Private State As MouseState = MouseState.None
    Private _tbType As topButtonType = topButtonType.Blank
    Public Property ButtonType() As topButtonType
        Get
            Return _tbType
        End Get
        Set(ByVal v As topButtonType)
            _tbType = v
            Invalidate()
        End Set
    End Property
    Private _defaultFunc As Boolean = True
    Public Property UseStandardFunction() As Boolean
        Get
            Return _defaultFunc
        End Get
        Set(ByVal v As Boolean)
            _defaultFunc = v
        End Set
    End Property

    Enum closeFunc As Byte
        CloseForm = 0
        CloseApp = 1
    End Enum
    Private _closeFunc As closeFunc = 0
    Public Property CloseButtonFunction() As closeFunc
        Get
            Return _closeFunc
        End Get
        Set(ByVal v As closeFunc)
            _closeFunc = v
        End Set
    End Property
    Protected Overrides Sub OnMouseEnter(ByVal e As System.EventArgs)
        MyBase.OnMouseEnter(e)
        State = MouseState.Over
        Invalidate()
    End Sub
    Protected Overrides Sub OnMouseDown(ByVal e As System.Windows.Forms.MouseEventArgs)
        MyBase.OnMouseDown(e)
        State = MouseState.Down
        Invalidate()
    End Sub
    Protected Overrides Sub OnMouseLeave(ByVal e As System.EventArgs)
        MyBase.OnMouseLeave(e)
        State = MouseState.None
        Invalidate()
    End Sub
    Protected Overrides Sub OnMouseUp(ByVal e As System.Windows.Forms.MouseEventArgs)
        MyBase.OnMouseUp(e)
        State = MouseState.Over
        Invalidate()
    End Sub
    Protected Overrides Sub OnPaintBackground(ByVal pevent As System.Windows.Forms.PaintEventArgs)
    End Sub
    Protected Overrides Sub OnTextChanged(ByVal e As System.EventArgs)
        MyBase.OnTextChanged(e)
        Invalidate()
    End Sub
    Sub buttonFunction() Handles Me.Click
        Select Case _defaultFunc
            Case True
                Select Case ButtonType
                    Case topButtonType.Close
                        Select Case CloseButtonFunction
                            Case 0
                                Me.Parent.FindForm.Close()
                            Case 1
                                Application.Exit()
                                End
                        End Select
                    Case topButtonType.Minimize
                        Me.Parent.FindForm.WindowState = FormWindowState.Minimized
                    Case topButtonType.Maximize
                        Me.Parent.FindForm.WindowState = FormWindowState.Maximized
                End Select
        End Select
    End Sub
#End Region

    Sub New()
        MyBase.New()
        BackColor = Color.FromArgb(38, 38, 38)
        Font = New Font("Verdana", 6.75F)
        Size = New Size(17, 17)
        DoubleBuffered = True
    End Sub

    Protected Overrides Sub OnPaint(ByVal e As System.Windows.Forms.PaintEventArgs)
        Dim G As Graphics = e.Graphics
        MyBase.OnPaint(e)

        Size = New Size(17, 17)
        G.Clear(Color.FromArgb(36, 34, 30))

        Select Case State
            Case MouseState.None 'Mouse None
                G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(36, 34, 30))), 2, Height - 1, Width - 4, Height - 1)
                Dim backGrad As New LinearGradientBrush(New Rectangle(1, 1, Width - 1, Height - 2), Color.FromArgb(10, 9, 8), Color.FromArgb(31, 29, 26), 90S)
                G.FillRectangle(backGrad, New Rectangle(1, 1, Width - 1, Height - 2))
                G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(46, 45, 44))), New Rectangle(1, 1, Width - 3, Height - 4))
                buttonColor = Color.FromArgb(163, 163, 162)
            Case MouseState.Over 'Mouse Hover
                G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(46, 44, 38))), 2, Height - 1, Width - 4, Height - 1)
                Dim backGrad As New LinearGradientBrush(New Rectangle(1, 1, Width - 1, Height - 2), Color.FromArgb(200, 0, 0), Color.FromArgb(250, 0, 0), 90S)
                G.FillRectangle(backGrad, New Rectangle(1, 1, Width - 1, Height - 2))
                G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(245, 0, 0))), New Rectangle(1, 1, Width - 3, Height - 4))
                buttonColor = Color.FromArgb(255, 255, 255)
            Case MouseState.Down 'Mouse Down
                G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(36, 34, 30))), 2, Height - 1, Width - 4, Height - 1)
                Dim backGrad As New LinearGradientBrush(New Rectangle(1, 1, Width - 1, Height - 2), Color.FromArgb(150, 0, 0), Color.FromArgb(210, 0, 0), 270S)
                G.FillRectangle(backGrad, New Rectangle(1, 1, Width - 1, Height - 2))
                G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(230, 0, 0))), New Rectangle(1, 1, Width - 3, Height - 4))

        End Select

        Select Case ButtonType
            Case topButtonType.Minimize
                G.FillRectangle(New SolidBrush(buttonColor), New Rectangle(4, 10, 9, 2))
            Case topButtonType.Maximize
                G.DrawRectangle(New Pen(New SolidBrush(buttonColor)), New Rectangle(4, 4, 8, 7))
                G.DrawLine(New Pen(New SolidBrush(buttonColor)), 4, 5, Width - 6, 5)
            Case topButtonType.Close
                G.DrawLine(New Pen(New SolidBrush(buttonColor), 2), 4, 4, 12, 11)
                G.DrawLine(New Pen(New SolidBrush(buttonColor), 2), 12, 4, 4, 11)
                G.DrawLine(New Pen(New SolidBrush(buttonColor)), 4, 4, 5, 5)
                G.DrawLine(New Pen(New SolidBrush(buttonColor)), 4, 11, 5, 10)
        End Select

        Dim glossGradient As New LinearGradientBrush(New Rectangle(1, 1, Width - 2, 7), Color.FromArgb(80, Color.White), Color.FromArgb(50, Color.White), 90S)
        G.FillRectangle(glossGradient, New Rectangle(1, 1, Width - 2, 7))
        G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(21, 20, 18))), New Rectangle(0, 0, Width - 1, Height - 2))
    End Sub
End Class


'--------------------- [ ProgressBar ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /ProgressBar ] ---------------------
Public Class ReactorProgressBar : Inherits Control

#Region " Control Help - Properties & Flicker Control "
    Private OFS As Integer = 0
    Private Speed As Integer = 50
    Private _Maximum As Integer = 100
    Public Property Maximum() As Integer
        Get
            Return _Maximum
        End Get
        Set(ByVal v As Integer)
            Select Case v
                Case Is < _Value
                    _Value = v
            End Select
            _Maximum = v
            Invalidate()
        End Set
    End Property
    Private _Value As Integer = 0
    Public Property Value() As Integer
        Get
            Select Case _Value
                Case 0
                    Return 1
                Case Else
                    Return _Value
            End Select
        End Get
        Set(ByVal v As Integer)
            Select Case v
                Case Is > _Maximum
                    v = _Maximum
            End Select
            _Value = v
            Invalidate()
        End Set
    End Property
    Protected Overrides Sub OnPaintBackground(ByVal pevent As System.Windows.Forms.PaintEventArgs)
    End Sub
    Protected Overrides Sub CreateHandle()
        MyBase.CreateHandle()
        ' Dim tmr As New Timer With {.Interval = Speed}
        ' AddHandler tmr.Tick, AddressOf Animate
        ' tmr.Start()
        Dim T As New Threading.Thread(AddressOf Animate)
        T.IsBackground = True
        T.Start()
    End Sub
    Sub Animate()
        While True
            If OFS <= Width Then : OFS += 1
            Else : OFS = 0
            End If
            Invalidate()
            Threading.Thread.Sleep(Speed)
        End While
    End Sub
#End Region

    Sub New()
        MyBase.New()
        DoubleBuffered = True
    End Sub

    Protected Overrides Sub OnPaint(ByVal e As System.Windows.Forms.PaintEventArgs)
        Dim G As Graphics = e.Graphics
        MyBase.OnPaint(e)

        Dim intValue As Integer = CInt(_Value / _Maximum * Width)
        G.Clear(Color.FromArgb(38, 38, 38))
        Try
            Dim backGrad As New LinearGradientBrush(New Rectangle(1, 1, intValue + 1, Height - 2), Color.FromArgb(10, 9, 8), Color.FromArgb(31, 29, 26), 90S)
            G.FillRectangle(backGrad, New Rectangle(1, 1, intValue + 1, Height - 2))
            Dim hatch As New HatchBrush(HatchStyle.WideUpwardDiagonal, Color.FromArgb(175, 190, 0, 0), Color.FromArgb(175, 255, 0, 0))
            G.RenderingOrigin = New Point(OFS, 0)
            G.FillRectangle(hatch, New Rectangle(1, 1, intValue + 1, Height - 2))
            Dim glossGradient As New LinearGradientBrush(New Rectangle(1, 1, intValue + 1, Height / 2 - 1), Color.FromArgb(80, Color.White), Color.FromArgb(50, Color.White), 90S)
            G.FillRectangle(glossGradient, New Rectangle(1, 1, intValue - 1, Height / 2 - 1))

            G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(10, 10, 10))), New Rectangle(0, 0, Width - 1, Height - 1))
            G.DrawRectangle((New Pen(New SolidBrush(Color.Black))), New Rectangle(1, 1, Width - 3, Height - 3))
            G.DrawRectangle((New Pen(New SolidBrush(Color.FromArgb(70, 70, 70)))), New Rectangle(0, 0, Width - 1, Height - 1))
            G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, Width, 0)
            G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, 0, Height)
            G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), Width - 1, 0, Width - 1, Height)
        Catch ex As Exception
        End Try
    End Sub
End Class


'--------------------- [ TextBox (Multi) ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /TextBox (Multi) ] ---------------------
Public Class ReactorMultiLineTextBox : Inherits Control
    Dim WithEvents txtbox As New TextBox

#Region " Control Help - Properties & Flicker Control "
    Private _maxchars As Integer = 32767
    Public Property MaxCharacters() As Integer
        Get
            Return _maxchars
        End Get
        Set(ByVal v As Integer)
            _maxchars = v
            Invalidate()
        End Set
    End Property
    Private _align As HorizontalAlignment
    Public Property TextAlign() As HorizontalAlignment
        Get
            Return _align
        End Get
        Set(ByVal v As HorizontalAlignment)
            _align = v
            Invalidate()
        End Set
    End Property
    Public Sub OnlyRead()
        txtbox.ReadOnly = True
    End Sub

    Public Sub ScrollToCaret()
        txtbox.ScrollToCaret()
    End Sub
    Protected Overrides Sub OnPaintBackground(ByVal pevent As System.Windows.Forms.PaintEventArgs)
    End Sub
    Protected Overrides Sub OnTextChanged(ByVal e As System.EventArgs)
        MyBase.OnTextChanged(e)
        Invalidate()
    End Sub
    Protected Overrides Sub OnBackColorChanged(ByVal e As System.EventArgs)
        MyBase.OnBackColorChanged(e)
        txtbox.BackColor = BackColor
        Invalidate()
    End Sub
    Protected Overrides Sub OnForeColorChanged(ByVal e As System.EventArgs)
        MyBase.OnForeColorChanged(e)
        txtbox.ForeColor = ForeColor
        Invalidate()
    End Sub
    Protected Overrides Sub OnSizeChanged(ByVal e As System.EventArgs)
        MyBase.OnSizeChanged(e)
        txtbox.Size = New Size(Width - 10, Height - 11)
    End Sub
    Protected Overrides Sub OnFontChanged(ByVal e As System.EventArgs)
        MyBase.OnFontChanged(e)
        txtbox.Font = Font
    End Sub
    Protected Overrides Sub OnGotFocus(ByVal e As System.EventArgs)
        MyBase.OnGotFocus(e)
        txtbox.Focus()
    End Sub
    Public Overloads Property SelectionStart() As Integer
        Get
            Return txtbox.SelectionStart
        End Get
        Set(ByVal v As Integer)
            txtbox.SelectionStart = v
        End Set
    End Property

    Public Overloads Property TextLength() As Integer
        Get
            Return txtbox.TextLength
        End Get
        Set(ByVal v As Integer)
        End Set
    End Property
    Sub TextChngTxtBox() Handles txtbox.TextChanged
        Text = txtbox.Text
    End Sub
    Sub TextChng() Handles MyBase.TextChanged
        txtbox.Text = Text
    End Sub
    Sub NewTextBox()
        With txtbox
            .Multiline = True
            .BackColor = BackColor
            .ForeColor = ForeColor
            .Text = String.Empty
            .TextAlign = HorizontalAlignment.Center
            .BorderStyle = BorderStyle.None
            .Location = New Point(2, 4)
            .Font = New Font("Verdana", 7.25)
            .Size = New Size(Width - 10, Height - 10)
        End With
    End Sub
#End Region

    Sub New()
        MyBase.New()

        NewTextBox()
        Controls.Add(txtbox)

        Text = ""
        BackColor = Color.FromArgb(37, 37, 37)
        ForeColor = Color.White
        Size = New Size(135, 35)
        DoubleBuffered = True
    End Sub

    Protected Overrides Sub OnPaint(ByVal e As System.Windows.Forms.PaintEventArgs)
        Dim G As Graphics = e.Graphics
        MyBase.OnPaint(e)

        txtbox.TextAlign = TextAlign

        G.FillRectangle(New SolidBrush(Color.FromArgb(37, 37, 37)), New Rectangle(1, 1, Width - 2, Height - 2))
        G.DrawRectangle((New Pen(New SolidBrush(Color.Black))), New Rectangle(1, 1, Width - 3, Height - 3))
        G.DrawRectangle((New Pen(New SolidBrush(Color.FromArgb(70, 70, 70)))), New Rectangle(0, 0, Width - 1, Height - 1))
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, Width, 0)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, 0, Height)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), Width - 1, 0, Width - 1, Height)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(31, 31, 31))), 2, 2, Width - 3, 2)
    End Sub
End Class

'--------------------- [ TextBox ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /TextBox ] ---------------------
Public Class ReactorTextBox : Inherits Control
    Public WithEvents txtbox As New TextBox

#Region " Control Help - Properties & Flicker Control "
    Private _passmask As Boolean = False
    Public Property UsePasswordMask() As Boolean
        Get
            Return _passmask
        End Get
        Set(ByVal v As Boolean)
            _passmask = v
            Invalidate()
        End Set
    End Property
    Private _maxchars As Integer = 32767
    Public Property MaxCharacters() As Integer
        Get
            Return _maxchars
        End Get
        Set(ByVal v As Integer)
            _maxchars = v
            Invalidate()
        End Set
    End Property
    Private _align As HorizontalAlignment
    Public Property TextAlign() As HorizontalAlignment
        Get
            Return _align
        End Get
        Set(ByVal v As HorizontalAlignment)
            _align = v
            Invalidate()
        End Set
    End Property
    Public Sub OnlyRead()
        txtbox.ReadOnly = True
    End Sub
    Protected Overrides Sub OnPaintBackground(ByVal pevent As System.Windows.Forms.PaintEventArgs)
    End Sub
    Protected Overrides Sub OnTextChanged(ByVal e As System.EventArgs)
        MyBase.OnTextChanged(e)
        Invalidate()
    End Sub
    Protected Overrides Sub OnBackColorChanged(ByVal e As System.EventArgs)
        MyBase.OnBackColorChanged(e)
        txtbox.BackColor = BackColor
        Invalidate()
    End Sub
    Protected Overrides Sub OnForeColorChanged(ByVal e As System.EventArgs)
        MyBase.OnForeColorChanged(e)
        txtbox.ForeColor = ForeColor
        Invalidate()
    End Sub
    Protected Overrides Sub OnFontChanged(ByVal e As System.EventArgs)
        MyBase.OnFontChanged(e)
        txtbox.Font = Font
    End Sub
    Protected Overrides Sub OnGotFocus(ByVal e As System.EventArgs)
        MyBase.OnGotFocus(e)
        txtbox.Focus()
    End Sub

    Sub TextChngTxtBox() Handles txtbox.TextChanged
        Text = txtbox.Text
    End Sub
    Sub TextChng() Handles MyBase.TextChanged
        txtbox.Text = Text
    End Sub
    Sub NewTextBox()
        With txtbox
            .Multiline = False
            .BackColor = BackColor
            .ForeColor = ForeColor
            .Text = String.Empty
            .TextAlign = HorizontalAlignment.Center
            .BorderStyle = BorderStyle.None
            .Location = New Point(5, 5)
            .Font = New Font("Verdana", 7.25)
            .Size = New Size(Width - 10, Height - 11)
            .UseSystemPasswordChar = UsePasswordMask
        End With

    End Sub
#End Region

    Sub New()
        MyBase.New()

        NewTextBox()
        Controls.Add(txtbox)

        Text = ""
        BackColor = Color.FromArgb(37, 37, 37)
        ForeColor = Color.White
        Size = New Size(135, 35)
        DoubleBuffered = True
    End Sub

    Protected Overrides Sub OnPaint(ByVal e As System.Windows.Forms.PaintEventArgs)
        Dim G As Graphics = e.Graphics
        MyBase.OnPaint(e)

        Height = txtbox.Height + 11
        With txtbox
            .Width = Width - 10
            .TextAlign = TextAlign
            .UseSystemPasswordChar = UsePasswordMask
        End With

        G.Clear(BackColor)
        G.FillRectangle(New SolidBrush(Color.FromArgb(37, 37, 37)), New Rectangle(1, 1, Width - 2, Height - 2))
        G.DrawRectangle((New Pen(New SolidBrush(Color.Black))), New Rectangle(1, 1, Width - 3, Height - 3))
        G.DrawRectangle((New Pen(New SolidBrush(Color.FromArgb(70, 70, 70)))), New Rectangle(0, 0, Width - 1, Height - 1))
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, Width, 0)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, 0, Height)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), Width - 1, 0, Width - 1, Height)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(31, 31, 31))), 2, 2, Width - 3, 2)
    End Sub
End Class

'--------------------- [ CheckedListBox ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /CheckedListBox ] ---------------------
Public Class ReactorCheckedListBox : Inherits Control
    Public WithEvents lstbox As New CheckedListBox
    Private __Items As String() = {""}

#Region " Control Help - Properties & Flicker Control "

    Protected Overrides Sub OnPaintBackground(ByVal pevent As System.Windows.Forms.PaintEventArgs)
    End Sub
    Protected Overrides Sub OnSizeChanged(ByVal e As System.EventArgs)
        MyBase.OnSizeChanged(e)
        lstbox.Size = New Size(Width - 6, Height - 6)
        Invalidate()
    End Sub
    Protected Overrides Sub OnBackColorChanged(ByVal e As System.EventArgs)
        MyBase.OnBackColorChanged(e)
        lstbox.BackColor = BackColor
        Invalidate()
    End Sub
    Protected Overrides Sub OnForeColorChanged(ByVal e As System.EventArgs)
        MyBase.OnForeColorChanged(e)
        lstbox.ForeColor = ForeColor
        Invalidate()
    End Sub
    Protected Overrides Sub OnFontChanged(ByVal e As System.EventArgs)
        MyBase.OnFontChanged(e)
        lstbox.Font = Font
    End Sub
    Protected Overrides Sub OnGotFocus(ByVal e As System.EventArgs)
        MyBase.OnGotFocus(e)
        lstbox.Focus()
    End Sub

    Public Property Items As String()
        Get
            Return __Items
            Invalidate()
        End Get
        Set(ByVal value As String())
            __Items = value
            lstbox.Items.Clear()
            Invalidate()
            lstbox.Items.AddRange(value)
            Invalidate()
        End Set
    End Property
    Public ReadOnly Property SelectedItem() As String
        Get
            Return lstbox.SelectedItem
        End Get
    End Property
    Public ReadOnly Property Count() As Integer
        Get
            Return lstbox.Items.Count
        End Get
    End Property
    Sub DrawItem(ByVal sender As Object, ByVal e As System.Windows.Forms.DrawItemEventArgs) Handles lstbox.DrawItem
        Try
            e.DrawBackground()
            e.Graphics.DrawString(lstbox.Items(e.Index).ToString(), _
            e.Font, New SolidBrush(lstbox.ForeColor), e.Bounds, StringFormat.GenericDefault)
            e.DrawFocusRectangle()
        Catch
        End Try
    End Sub
    Sub AddRange(ByVal Items As Object())
        lstbox.Items.Remove("")
        lstbox.Items.AddRange(Items)
        Invalidate()
    End Sub
    Sub AddItem(ByVal Item As Object)
        If lstbox.Items.Contains("") Then
            lstbox.Items.Remove("")
        End If
        lstbox.Items.Add(Item)
        Invalidate()
    End Sub
    Sub RemoveItem(ByVal Item As Object)
        lstbox.Items.Remove(Item)
        Invalidate()
    End Sub
    Sub NewListBox()
        lstbox.Size = New Size(126, 96)
        lstbox.BorderStyle = BorderStyle.None
        lstbox.DrawMode = DrawMode.OwnerDrawVariable
        lstbox.Location = New Point(4, 4)
        lstbox.ForeColor = Color.FromArgb(216, 216, 216)
        lstbox.BackColor = Color.FromArgb(38, 38, 38)
        lstbox.Items.Clear()
    End Sub

#End Region

    Sub New()
        MyBase.New()

        NewListBox()
        Controls.Add(lstbox)
        Size = New Size(131, 101)
        BackColor = Color.FromArgb(38, 38, 38)
        DoubleBuffered = True
    End Sub

    Protected Overrides Sub OnPaint(ByVal e As System.Windows.Forms.PaintEventArgs)
        Dim G As Graphics = e.Graphics
        MyBase.OnPaint(e)

        G.Clear(BackColor)
        G.FillRectangle(New SolidBrush(Color.FromArgb(37, 37, 37)), New Rectangle(1, 1, Width - 2, Height - 2))
        G.DrawRectangle((New Pen(New SolidBrush(Color.Black))), New Rectangle(1, 1, Width - 3, Height - 3))
        G.DrawRectangle((New Pen(New SolidBrush(Color.FromArgb(70, 70, 70)))), New Rectangle(0, 0, Width - 1, Height - 1))
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, Width, 0)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, 0, Height)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), Width - 1, 0, Width - 1, Height)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(31, 31, 31))), 2, 2, Width - 3, 2)
    End Sub
End Class

'--------------------- [ ListBox ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /ListBox ] ---------------------
Public Class ReactorListBox : Inherits Control
    Public WithEvents lstbox As New ListBox
    Private __Items As String() = {""}

#Region " Control Help - Properties & Flicker Control "

    Protected Overrides Sub OnPaintBackground(ByVal pevent As System.Windows.Forms.PaintEventArgs)
    End Sub
    Protected Overrides Sub OnSizeChanged(ByVal e As System.EventArgs)
        MyBase.OnSizeChanged(e)
        lstbox.Size = New Size(Width - 6, Height - 6)
        Invalidate()
    End Sub
    Protected Overrides Sub OnBackColorChanged(ByVal e As System.EventArgs)
        MyBase.OnBackColorChanged(e)
        lstbox.BackColor = BackColor
        Invalidate()
    End Sub
    Protected Overrides Sub OnForeColorChanged(ByVal e As System.EventArgs)
        MyBase.OnForeColorChanged(e)
        lstbox.ForeColor = ForeColor
        Invalidate()
    End Sub
    Protected Overrides Sub OnFontChanged(ByVal e As System.EventArgs)
        MyBase.OnFontChanged(e)
        lstbox.Font = Font
    End Sub
    Protected Overrides Sub OnGotFocus(ByVal e As System.EventArgs)
        MyBase.OnGotFocus(e)
        lstbox.Focus()
    End Sub

    Public Property Items As String()
        Get
            Return __Items
            Invalidate()
        End Get
        Set(ByVal value As String())
            __Items = value
            lstbox.Items.Clear()
            Invalidate()
            lstbox.Items.AddRange(value)
            Invalidate()
        End Set
    End Property
    Public ReadOnly Property SelectedItem() As String
        Get
            Return lstbox.SelectedItem
        End Get
    End Property
    Public ReadOnly Property Count() As Integer
        Get
            Return lstbox.Items.Count
        End Get
    End Property
    Sub bupdate()
        lstbox.BeginUpdate()
    End Sub
    Sub eupdate()
        lstbox.EndUpdate()
    End Sub
    Sub DrawItem(ByVal sender As Object, ByVal e As System.Windows.Forms.DrawItemEventArgs) Handles lstbox.DrawItem
        Try
            e.DrawBackground()
            e.Graphics.DrawString(lstbox.Items(e.Index).ToString(), _
            e.Font, New SolidBrush(lstbox.ForeColor), e.Bounds, StringFormat.GenericDefault)
            e.DrawFocusRectangle()
        Catch
        End Try
    End Sub
    Sub AddRange(ByVal Items As Object())
        lstbox.Items.Remove("")
        lstbox.Items.AddRange(Items)
        Invalidate()
    End Sub
    Sub AddItem(ByVal Item As Object)
        If lstbox.Items.Contains("") Then
            lstbox.Items.Remove("")
        End If
        lstbox.Items.Add(Item)
        Invalidate()
    End Sub
    Sub RemoveItem(ByVal Item As Object)
        lstbox.Items.Remove(Item)
        Invalidate()
    End Sub
    Sub NewListBox()
        lstbox.Size = New Size(126, 96)
        lstbox.BorderStyle = BorderStyle.None
        lstbox.DrawMode = DrawMode.OwnerDrawVariable
        lstbox.Location = New Point(4, 4)
        lstbox.ForeColor = Color.FromArgb(216, 216, 216)
        lstbox.BackColor = Color.FromArgb(38, 38, 38)
        lstbox.Items.Clear()
    End Sub

#End Region

    Sub New()
        MyBase.New()

        NewListBox()
        Controls.Add(lstbox)
        Size = New Size(131, 101)
        BackColor = Color.FromArgb(38, 38, 38)
        DoubleBuffered = True
    End Sub

    Protected Overrides Sub OnPaint(ByVal e As System.Windows.Forms.PaintEventArgs)
        Dim G As Graphics = e.Graphics
        MyBase.OnPaint(e)

        G.Clear(BackColor)
        G.FillRectangle(New SolidBrush(Color.FromArgb(37, 37, 37)), New Rectangle(1, 1, Width - 2, Height - 2))
        G.DrawRectangle((New Pen(New SolidBrush(Color.Black))), New Rectangle(1, 1, Width - 3, Height - 3))
        G.DrawRectangle((New Pen(New SolidBrush(Color.FromArgb(70, 70, 70)))), New Rectangle(0, 0, Width - 1, Height - 1))
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, Width, 0)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, 0, Height)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), Width - 1, 0, Width - 1, Height)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(31, 31, 31))), 2, 2, Width - 3, 2)
    End Sub
End Class


'--------------------- [ ComboBox ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /ComboBox ] ---------------------
Public Class ReactorComboBox : Inherits ComboBox

#Region " Control Help - Properties & Flicker Control "
    Protected Overrides Sub OnPaintBackground(ByVal pevent As System.Windows.Forms.PaintEventArgs)
    End Sub
    Private _StartIndex As Integer = 0
    Public Property StartIndex As Integer
        Get
            Return _StartIndex
        End Get
        Set(ByVal value As Integer)
            _StartIndex = value
            Try
                MyBase.SelectedIndex = value
            Catch
            End Try
            Invalidate()
        End Set
    End Property
    Private LightBlack As Color = Color.FromArgb(37, 37, 37)
    Private LighterBlack As Color = Color.FromArgb(60, 60, 60)
    Sub ReplaceItem(ByVal sender As System.Object, ByVal e As System.Windows.Forms.DrawItemEventArgs) Handles Me.DrawItem
        e.DrawBackground()
        Try
            If (e.State And DrawItemState.Selected) = DrawItemState.Selected Then
                e.Graphics.FillRectangle(New SolidBrush(Color.FromArgb(229, 88, 0)), e.Bounds) '37 37 37
            End If
            Using b As New SolidBrush(e.ForeColor)
                e.Graphics.DrawString(MyBase.GetItemText(MyBase.Items(e.Index)), e.Font, b, e.Bounds)
            End Using
        Catch
        End Try
        e.DrawFocusRectangle()
    End Sub
    Protected Sub DrawTriangle(ByVal Clr As Color, ByVal FirstPoint As Point, ByVal SecondPoint As Point, ByVal ThirdPoint As Point, ByVal G As Graphics)
        Dim points As New List(Of Point)()
        points.Add(FirstPoint)
        points.Add(SecondPoint)
        points.Add(ThirdPoint)
        G.FillPolygon(New SolidBrush(Clr), points.ToArray)
    End Sub
#End Region

    Sub New()
        MyBase.New()
        SetStyle(ControlStyles.AllPaintingInWmPaint Or _
        ControlStyles.ResizeRedraw Or _
        ControlStyles.UserPaint Or _
        ControlStyles.DoubleBuffer, True)
        DrawMode = Windows.Forms.DrawMode.OwnerDrawFixed
        BackColor = Color.FromArgb(45, 45, 45)
        ForeColor = Color.White
        DropDownStyle = ComboBoxStyle.DropDownList
        DoubleBuffered = True
        Invalidate()
    End Sub

    Protected Overrides Sub OnPaint(ByVal e As PaintEventArgs)
        MyBase.OnPaint(e)
        Dim G As Graphics = e.Graphics
        Dim T As LinearGradientBrush = New LinearGradientBrush(New Rectangle(0, 0, Width, 10), Color.FromArgb(62, Color.White), Color.FromArgb(30, Color.White), 90S)
        Dim DrawCornersBrush As SolidBrush
        DrawCornersBrush = New SolidBrush(Color.FromArgb(37, 37, 37))
        Try
            With G
                .SmoothingMode = SmoothingMode.HighQuality
                .Clear(Color.FromArgb(37, 37, 37))
                .FillRectangle(T, New Rectangle(Width - 20, 0, Width, 9))
                .DrawLine(Pens.Black, Width - 20, 0, Width - 20, Height)
                Try
                    '.DrawString(Items(SelectedIndex).ToString, Font, Brushes.White, New Rectangle(New Point(3, 3), New Size(Width - 18, Height)))
                    .DrawString(Text, Font, Brushes.White, New Rectangle(3, 0, Width - 20, Height), New StringFormat With {.LineAlignment = StringAlignment.Center, .Alignment = StringAlignment.Near})
                Catch
                End Try

                .DrawLine(New Pen(New SolidBrush(Color.FromArgb(37, 37, 37))), 0, 0, 0, 0)
                .DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(0, 0, 0))), New Rectangle(1, 1, Width - 3, Height - 3))

                .DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, Width, 0)
                .DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, 0, Height)
                .DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), Width - 1, 0, Width - 1, Height)
                .DrawLine(New Pen(New SolidBrush(Color.FromArgb(70, 70, 70))), 0, Height - 1, Width, Height - 1)

                DrawTriangle(Color.White, New Point(Width - 14, 8), New Point(Width - 7, 8), New Point(Width - 11, 11), G)
            End With
        Catch
        End Try
    End Sub
End Class


'--------------------- [ TabControl ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /TabControl ] ---------------------
Public Class ReactorTabControl : Inherits TabControl

#Region " Control Help - Properties & Flicker Control "
    Private DrawGradientBrush, DrawGradientBrush2 As LinearGradientBrush
    Private _TabBrColor As Color
    Public Property TabBorderColor() As Color
        Get
            Return _TabBrColor
        End Get
        Set(ByVal v As Color)
            _TabBrColor = v
            Invalidate()
        End Set
    End Property
    Private _ControlBColor As Color
    Public Property TabTextColor() As Color
        Get
            Return _ControlBColor
        End Get
        Set(ByVal v As Color)
            _ControlBColor = v
            Invalidate()
        End Set
    End Property
    Protected Overrides Sub OnPaintBackground(ByVal pevent As System.Windows.Forms.PaintEventArgs)
    End Sub
#End Region

    Sub New()
        MyBase.New()
        SetStyle(ControlStyles.AllPaintingInWmPaint Or _
        ControlStyles.ResizeRedraw Or _
        ControlStyles.UserPaint Or _
        ControlStyles.DoubleBuffer, True)

        TabBorderColor = Color.White
        TabTextColor = Color.White

    End Sub

    Protected Overrides Sub OnPaint(ByVal e As PaintEventArgs)
        Try
            Dim G As Graphics = e.Graphics
            MyBase.OnPaint(e)

            Dim r2 As New Rectangle(0, 0, Width - 1, 25)
            Dim r3 As New Rectangle(0, 0, Width - 1, 25)
            Dim r4 As New Rectangle(2, 0, Width - 1, 13)
            Dim ItemBounds As Rectangle
            Dim TextBrush As New SolidBrush(Color.Empty)
            Dim TabBrush As New SolidBrush(Color.DimGray)
            Dim dgb2 As New LinearGradientBrush(r4, Color.FromArgb(50, Color.White), Color.FromArgb(25, Color.White), 90S)

            G.Clear(Color.FromArgb(32, 32, 32))
            DrawGradientBrush2 = New LinearGradientBrush(r3, Color.FromArgb(10, 10, 10), Color.FromArgb(50, 50, 50), 90S)
            G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(28, 28, 28))), New Rectangle(1, 1, Width - 3, Height - 3))
            Try
                G.FillRectangle(DrawGradientBrush2, r2)
                G.FillRectangle(dgb2, r4)
            Catch
            End Try
            For TabItemIndex As Integer = 0 To Me.TabCount - 1
                ItemBounds = Me.GetTabRect(TabItemIndex)

                If CBool(TabItemIndex And 1) Then
                    TabBrush.Color = Color.Transparent
                Else
                    TabBrush.Color = Color.Transparent
                End If
                G.FillRectangle(TabBrush, ItemBounds)
                Dim BorderPen As Pen
                If TabItemIndex = SelectedIndex Then
                    BorderPen = New Pen(Color.Transparent, 1)
                    Dim dgb As New LinearGradientBrush(New Rectangle(ItemBounds.Location.X + 3, ItemBounds.Location.Y + 3, ItemBounds.Width - 8, ItemBounds.Height - 6), Color.FromArgb(175, 175, 0, 0), Color.FromArgb(175, 255, 0, 0), 90S)
                    Dim gloss As New LinearGradientBrush(New Rectangle(ItemBounds.Location.X + 3, ItemBounds.Location.Y + 3, ItemBounds.Width - 8, ItemBounds.Height / 2 - 5), Color.FromArgb(80, Color.White), Color.FromArgb(20, Color.White), 90S)
                    G.FillRectangle(dgb, New Rectangle(ItemBounds.Location.X + 3, ItemBounds.Location.Y + 3, ItemBounds.Width - 8, ItemBounds.Height - 6))
                    G.FillRectangle(gloss, New Rectangle(ItemBounds.Location.X + 3, ItemBounds.Location.Y + 3, ItemBounds.Width - 8, ItemBounds.Height / 2 - 4))
                    G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(10, 10, 10))), New Rectangle(ItemBounds.Location.X + 3, ItemBounds.Location.Y + 3, ItemBounds.Width - 8, ItemBounds.Height - 6))
                    G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(100, 230, 90, 0))), New Rectangle(ItemBounds.Location.X + 4, ItemBounds.Location.Y + 4, ItemBounds.Width - 10, ItemBounds.Height - 8))
                    Dim r1 As New Rectangle(1, 1, Width - 1, 3)
                Else
                    BorderPen = New Pen(Color.Transparent, 1)
                End If

                G.DrawRectangle(BorderPen, New Rectangle(ItemBounds.Location.X + 3, ItemBounds.Location.Y + 1, ItemBounds.Width - 8, ItemBounds.Height - 4))

                BorderPen.Dispose()

                Dim sf As New StringFormat
                sf.LineAlignment = StringAlignment.Center
                sf.Alignment = StringAlignment.Center

                If Me.SelectedIndex = TabItemIndex Then
                    TextBrush.Color = TabTextColor
                Else
                    TextBrush.Color = Color.DimGray
                End If
                G.DrawString( _
                Me.TabPages(TabItemIndex).Text, _
                Me.Font, TextBrush, _
                RectangleF.op_Implicit(Me.GetTabRect(TabItemIndex)), sf)
                Try
                    Me.TabPages(TabItemIndex).BackColor = Color.FromArgb(32, 32, 32)
                Catch
                End Try
            Next
            Try
                For Each tabpg As TabPage In Me.TabPages
                    tabpg.BorderStyle = BorderStyle.None
                Next
            Catch
            End Try

            G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(10, 10, 10))), 2, 24, Width - 2, 24)
            G.DrawRectangle((New Pen(New SolidBrush(Color.Black))), New Rectangle(1, 1, Width - 3, Height - 3))
            G.DrawRectangle((New Pen(New SolidBrush(Color.FromArgb(70, 70, 70)))), New Rectangle(0, 0, Width - 1, Height - 1))
            G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, Width, 0)
            G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, 0, Height)
            G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), Width - 1, 0, Width - 1, Height)
            G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(31, 31, 31))), 2, 2, Width - 3, 2)
        Catch
        End Try
    End Sub
End Class


'--------------------- [ CheckBox ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /CheckBox ] ---------------------
<DefaultEvent("CheckedChanged")> Public Class ReactorCheckBox : Inherits Control

#Region " Control Help - MouseState & Flicker Control"
    Private State As MouseState = MouseState.None
    Protected Overrides Sub OnMouseEnter(ByVal e As System.EventArgs)
        MyBase.OnMouseEnter(e)
        State = MouseState.Over
        Invalidate()
    End Sub
    Protected Overrides Sub OnMouseDown(ByVal e As System.Windows.Forms.MouseEventArgs)
        MyBase.OnMouseDown(e)
        State = MouseState.Down
        Invalidate()
    End Sub
    Protected Overrides Sub OnMouseLeave(ByVal e As System.EventArgs)
        MyBase.OnMouseLeave(e)
        State = MouseState.None
        Invalidate()
    End Sub
    Protected Overrides Sub OnMouseUp(ByVal e As System.Windows.Forms.MouseEventArgs)
        MyBase.OnMouseUp(e)
        State = MouseState.Over
        Invalidate()
    End Sub
    Protected Overrides Sub OnPaintBackground(ByVal pevent As System.Windows.Forms.PaintEventArgs)
    End Sub
    Protected Overrides Sub OnTextChanged(ByVal e As System.EventArgs)
        MyBase.OnTextChanged(e)
        Invalidate()
    End Sub
    Private _Checked As Boolean
    Property Checked() As Boolean
        Get
            Return _Checked
        End Get
        Set(ByVal value As Boolean)
            _Checked = value
            Invalidate()
        End Set
    End Property

    Protected Overrides Sub OnClick(ByVal e As System.EventArgs)
        _Checked = Not _Checked
        RaiseEvent CheckedChanged(Me)
        MyBase.OnClick(e)
    End Sub
    Event CheckedChanged(ByVal sender As Object)
#End Region

    Sub New()
        MyBase.New()
        BackColor = Color.FromArgb(38, 38, 38)
        ForeColor = Color.White
    End Sub

    Protected Overrides Sub OnPaint(ByVal e As System.Windows.Forms.PaintEventArgs)
        MyBase.OnPaint(e)
        Dim G As Graphics = e.Graphics

        Height = 16

        G.Clear(BackColor)

        Dim bordColor As Color = Color.FromArgb(46, 45, 44)
        Dim backGrad As New LinearGradientBrush(New Rectangle(1, 1, 14, Height - 2), Color.FromArgb(10, 9, 8), Color.FromArgb(31, 29, 26), 90S)
        Dim glossGradient As New LinearGradientBrush(New Rectangle(1, 1, 13, Height / 2 - 2), Color.FromArgb(80, Color.White), Color.FromArgb(50, Color.White), 90S)

        If _Checked Then
            bordColor = Color.FromArgb(225, 0, 0)
            backGrad = New LinearGradientBrush(New Rectangle(1, 1, 14, Height - 2), Color.FromArgb(200, 0, 0), Color.FromArgb(255, 0, 0), 90S)
        Else
            bordColor = Color.FromArgb(46, 45, 44)
            backGrad = New LinearGradientBrush(New Rectangle(1, 1, 14, Height - 2), Color.FromArgb(24, 24, 24), Color.FromArgb(30, 30, 30), 90S)
        End If

        G.DrawString(Text, Font, New SolidBrush(ForeColor), New Point(18, Height / 2 + (Font.Height) - 18))

        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(36, 34, 30))), 2, Height - 1, 14, Height - 1)
        G.FillRectangle(backGrad, New Rectangle(1, 1, 14, Height - 2))
        G.DrawRectangle(New Pen(New SolidBrush(bordColor)), New Rectangle(1, 1, 12, Height - 4))
        If _Checked Then : G.FillRectangle(glossGradient, New Rectangle(1, 1, 13, Height / 2 - 2))
        End If
        G.DrawRectangle(New Pen(New SolidBrush(Color.FromArgb(10, 10, 10))), New Rectangle(0, 0, 14, Height - 2))
    End Sub

End Class


'--------------------- [ RadioButton ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /RadioButton ] ---------------------
<DefaultEvent("CheckedChanged")> Public Class ReactorRadioButton : Inherits Control

#Region " Control Help - MouseState & Flicker Control"
    Private R1 As Rectangle
    Private G1 As LinearGradientBrush

    Private State As MouseState = MouseState.None
    Protected Overrides Sub OnMouseEnter(ByVal e As System.EventArgs)
        MyBase.OnMouseEnter(e)
        State = MouseState.Over
        Invalidate()
    End Sub
    Protected Overrides Sub OnMouseDown(ByVal e As System.Windows.Forms.MouseEventArgs)
        MyBase.OnMouseDown(e)
        State = MouseState.Down
        Invalidate()
    End Sub
    Protected Overrides Sub OnMouseLeave(ByVal e As System.EventArgs)
        MyBase.OnMouseLeave(e)
        State = MouseState.None
        Invalidate()
    End Sub
    Protected Overrides Sub OnMouseUp(ByVal e As System.Windows.Forms.MouseEventArgs)
        MyBase.OnMouseUp(e)
        State = MouseState.Over
        Invalidate()
    End Sub
    Protected Overrides Sub OnPaintBackground(ByVal pevent As System.Windows.Forms.PaintEventArgs)
    End Sub
    Protected Overrides Sub OnTextChanged(ByVal e As System.EventArgs)
        MyBase.OnTextChanged(e)
        Invalidate()
    End Sub
    Private _Checked As Boolean
    Property Checked() As Boolean
        Get
            Return _Checked
        End Get
        Set(ByVal value As Boolean)
            _Checked = value
            InvalidateControls()
            RaiseEvent CheckedChanged(Me)
            Invalidate()
        End Set
    End Property
    Protected Overrides Sub OnClick(ByVal e As EventArgs)
        If Not _Checked Then Checked = True
        MyBase.OnClick(e)
    End Sub
    Event CheckedChanged(ByVal sender As Object)
    Protected Overrides Sub OnCreateControl()
        MyBase.OnCreateControl()
        InvalidateControls()
    End Sub
    Private Sub InvalidateControls()
        If Not IsHandleCreated OrElse Not _Checked Then Return

        For Each C As Control In Parent.Controls
            If C IsNot Me AndAlso TypeOf C Is ReactorRadioButton Then
                DirectCast(C, ReactorRadioButton).Checked = False
            End If
        Next
    End Sub
#End Region

    Sub New()
        MyBase.New()
        BackColor = Color.FromArgb(38, 38, 38)
        ForeColor = Color.White
    End Sub

    Protected Overrides Sub OnPaint(ByVal e As System.Windows.Forms.PaintEventArgs)
        MyBase.OnPaint(e)
        Dim G As Graphics = e.Graphics

        Height = 16
        G.Clear(BackColor)
        G.SmoothingMode = SmoothingMode.HighQuality
        R1 = New Rectangle(2, 0, 13, 13)
        If _Checked Then
            G1 = New LinearGradientBrush(R1, Color.FromArgb(220, 200, 0, 0), Color.FromArgb(220, 255, 0, 0), 90S)
        Else
            G1 = New LinearGradientBrush(R1, Color.FromArgb(24, 24, 24), Color.FromArgb(30, 30, 30), 90S)
        End If
        G.FillEllipse(G1, R1)
        If State = MouseState.Over Then
            R1 = New Rectangle(2, 0, 13, 13)
            G.FillEllipse(New SolidBrush(Color.FromArgb(5, Color.White)), R1)
        End If
        If _Checked Then
            R1 = New Rectangle(4, 1, 9, 6)
            G1 = New LinearGradientBrush(R1, Color.FromArgb(80, 255, 255, 255), Color.FromArgb(30, 255, 255, 255), 90S)
            G.FillEllipse(G1, R1)
            G.DrawEllipse(New Pen(New SolidBrush(Color.FromArgb(250, 0, 0))), 3, 1, 11, 11)
        End If
        G.DrawString(Text, Font, New SolidBrush(ForeColor), 18, 1)
        G.DrawEllipse(Pens.Black, 2, 0, 13, 13)
        G.DrawEllipse(New Pen(Color.FromArgb(15, Color.White)), 1, -1, 15, 15)
    End Sub

End Class


'--------------------- [ GroupBox ] --------------------
'Creator: oVERT3Xo
'Site: hackforums.net
'Contact: oVERT3Xo@hotmail.com
'Created: 11/22/2011
'Changed: 11/23/2011
'-------------------- [ /GroupBox ] ---------------------
Public Class ReactorGroupBox : Inherits ContainerControl
#Region " Control Help - Properties & Flicker Control"
    Protected Overrides Sub OnPaintBackground(ByVal pevent As System.Windows.Forms.PaintEventArgs)
    End Sub
    Protected Overrides Sub OnTextChanged(ByVal e As System.EventArgs)
        MyBase.OnTextChanged(e)
        Invalidate()
    End Sub
#End Region

    Sub New()
        MyBase.New()
        BackColor = Color.FromArgb(33, 33, 33)
    End Sub

    Protected Overrides Sub OnPaint(ByVal e As System.Windows.Forms.PaintEventArgs)
        MyBase.OnPaint(e)
        Dim G As Graphics = e.Graphics

        G.Clear(BackColor)
        G.SmoothingMode = SmoothingMode.HighQuality

        Dim DrawGradientBrush2 As New LinearGradientBrush(New Rectangle(0, 0, Width, 24), Color.FromArgb(10, 10, 10), Color.FromArgb(50, 50, 50), 90S)

        G.FillRectangle(DrawGradientBrush2, New Rectangle(0, 0, Width, 24))
        Dim gloss As New LinearGradientBrush(New Rectangle(0, 0, Width, 12), Color.FromArgb(60, Color.White), Color.FromArgb(20, Color.White), 90S)
        G.FillRectangle(gloss, New Rectangle(0, 0, Width, 12))
        G.DrawLine(Pens.Black, 0, 24, Width, 24)

        G.DrawRectangle((New Pen(New SolidBrush(Color.Black))), New Rectangle(1, 1, Width - 3, Height - 3))
        G.DrawRectangle((New Pen(New SolidBrush(Color.FromArgb(70, 70, 70)))), New Rectangle(0, 0, Width - 1, Height - 1))
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, Width, 0)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), 0, 0, 0, Height)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(45, 45, 45))), Width - 1, 0, Width - 1, Height)
        G.DrawLine(New Pen(New SolidBrush(Color.FromArgb(31, 31, 31))), 2, 2, Width - 3, 2)

        G.DrawString(Text, Font, Brushes.Black, Width / 2 - (3 * Text.Length) + 1, 6)
        G.DrawString(Text, Font, Brushes.White, Width / 2 - (3 * Text.Length) + 1, 7)
    End Sub
End Class
