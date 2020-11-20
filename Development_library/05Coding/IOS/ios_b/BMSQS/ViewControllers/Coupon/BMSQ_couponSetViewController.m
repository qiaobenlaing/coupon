//
//  BMSQ_couponSetViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/16.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_couponSetViewController.h"
#import "BMSQ_FindSetCouponViewController.h"


#define SubCellHeight 35.0
#define TAG_FUNCTION   1000//每张可以
#define TAG_INSTEAD_PRICE  3001//减免多少或者每张面值多少
#define TAG_TOTAL_VOLUME   3002//发行多少张
#define TAG_AVAILABLE_PRICE  3003 //满多少可以使用
#define TAG_NBR_PER_PERSON  3004//每人最多领取多少张
#define TAG_LIMITEDNBR   3005//每单限用多少张
#define TAG_SEND_REQUIRED //每满多少金额送一张优惠券
#define TAG_LIMITED_SEND_NBR //每单最多送多少张

@interface BMSQ_couponSetViewController ()<UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate,UITextViewDelegate, UIAlertViewDelegate>

@property (nonatomic, strong)UITableView *myTableView;


@property (nonatomic, strong)NSString *Desctription; //描述
@property (nonatomic, strong)NSString *totalVolume;  //发几张
@property (nonatomic, strong)NSString *Value;  //满N元


@property (nonatomic, strong)NSString *disCount; // 打几折

@property (nonatomic, assign)BOOL isTotal; //发行的数量是否限制
@property (nonatomic, assign)BOOL isnbrPerPerson;  //是否限制领取/购买的数量


@property (nonatomic, strong)NSString *nbrPerPerson;    //每人限领张数 1
@property (nonatomic, strong)NSString *limitedNbr;      //单笔限使用的张数 1


@property (nonatomic, strong)UIView *selePickerView;
@property (nonatomic, strong)UILabel *seleDateLable;
@property (nonatomic, strong)NSString *seleDateStr;

@property (nonatomic, strong)NSString *startDateStr;
@property (nonatomic, assign)BOOL isStartDate;
@property (nonatomic, strong)NSString *endDateStr;
@property (nonatomic, assign)BOOL isEndDate;


@property (nonatomic, strong)UIView *seleTimePickerView;
@property (nonatomic, strong)UILabel *seleTimeLable;
@property (nonatomic, strong)NSString *seleTimeStr;

@property (nonatomic, strong)NSString *startTimeStr;
@property (nonatomic, assign)BOOL isStartTime;
@property (nonatomic, strong)NSString *endTimeStr;
@property (nonatomic, assign)BOOL isEndTime;

@property (nonatomic, strong)NSString *startReceiveDateStr; //领取开始时间
@property (nonatomic, assign)BOOL isStartReceDate; //是否选择了 领取开始时间 YES是 NO否
@property (nonatomic, strong)NSString *endReceiveDateStr; // 领取结束时间
@property (nonatomic, assign)BOOL isEndReceDate; //是否选择了 领取结束时间 YES是 NO否

@property (nonatomic, strong)NSString *GetConditions; //赠送条件



@property (nonatomic, strong)NSString *remark;

@property (nonatomic, assign)float remarkH;


@property (nonatomic, strong)NSString *sendRequired;
@property (nonatomic, strong)NSString *discountPercent;
@property (nonatomic, strong)NSString *insteadPrice;
@property (nonatomic, strong)NSString *availablePrice;
@property (nonatomic, strong)NSString *payPrice; //得到一张优惠券需要多少钱

@property (nonatomic, strong)NSString *isSend;

@property (nonatomic, assign)BOOL isFindButton;  //预览
@property (nonatomic, assign)BOOL isDateButton;   //选择日期
@property (nonatomic, assign)BOOL isTimeButton;   //选择时间

@property (nonatomic, assign)BOOL isSeleSend;  //选择满就送
@property (nonatomic, assign)BOOL isSeleEndTake; //选择领取日期



@property (nonatomic, strong)NSString *limitedSendNbr;  //单笔消费最多送的张数 1


//比较使用日期是否合法
@property (nonatomic, strong)NSDate *startDate;
@property (nonatomic, strong)NSDate *endDate;





@end

@implementation BMSQ_couponSetViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavTitle:self.myTitle];
    
    
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(compareDateSuecc) name:@"compareDateSuecc" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(compareTimeSuecc) name:@"compareTimeSuecc" object:nil];

    
    self.Desctription = @"";
    //默认领取时间
    self.isDateButton = YES;
    NSDate *date = [NSDate date];
    self.startDate = date;
    NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
    [outputFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *timestamp_str = [outputFormatter stringFromDate:date];
    self.startDateStr = timestamp_str;
    
    NSDate *nextDate = [NSDate dateWithTimeInterval:24*60*60*7 sinceDate:date];
    self.endDate = nextDate;
    timestamp_str = [outputFormatter stringFromDate:nextDate];
    self.endDateStr = timestamp_str;
    
    self.isTotal = NO;   //发行的数量
    self.isnbrPerPerson = NO; //是否限制单人领取的数量
    
    //默认使用时间
    self.isTimeButton = YES;
    self.startTimeStr = @"09:00";
    self.endTimeStr = @"18:00";
    
    self.isFindButton = NO;
    
    
    self.remarkH = 100;
    
    self.limitedSendNbr = @"1";
    //默认领取日期
    self.isSeleEndTake = YES;
    self.startReceiveDateStr = self.startDateStr;
    self.endReceiveDateStr = self.endDateStr;


    self.myTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y)];
    self.myTableView.backgroundColor = self.view.backgroundColor;
    self.myTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.myTableView.delegate = self;
    self.myTableView.dataSource = self;
    [self.view addSubview:self.myTableView];
    
    self.myTableView.keyboardDismissMode  = UIScrollViewKeyboardDismissModeInteractive;
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillHide:)
                                                 name:UIKeyboardWillHideNotification
                                               object:nil];
 }


-(void)keyboardWillHide:(NSNotification *)nt{
    [UIView animateWithDuration:0.2 animations:^{
        self.myTableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH,APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
        
    } completion:^(BOOL finished) {
        
    }];

}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
   
    return 5;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row == 0) {
        
        if(self.type == 3 || self.type == 4){
            return 10+ 35 + 35+45+35+45+35+5;
            
            
        }
        if (self.type == 7 || self.type == 8) {
            return SubCellHeight*4+20+10;
        }
        
        else{
            return 81;
        }
    }else if (indexPath.row == 1){
        if (self.type == 1||self.type== 3||self.type==4|| self.type == 7 || self.type == 8 ) {
            return  147;
        }else{
            return 81;
        }
    }else if (indexPath.row ==2){
        if (self.type == 8) {
            return SubCellHeight *2 ;
        }
        return SubCellHeight * 5;
        
    }else if (indexPath.row == 3){
        return 170;
    }else if (indexPath.row == 4){
        return 55;
    }
    return 90;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.row == 4){ // 预览
        
        static NSString *identifer = @"N_section_4";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifer];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifer];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH-30, 35)];
            [button setTitle:@"预览" forState:UIControlStateNormal];
            [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            button.titleLabel.font = [UIFont boldSystemFontOfSize:16.f];
            button.center = CGPointMake(APP_VIEW_WIDTH/2, 45/2+10);
            button.tag = 100;
            [button addTarget:self action:@selector(findCoupon) forControlEvents:UIControlEventTouchUpInside];
            [cell.contentView addSubview:button];
        }
        UIButton *findButton =(UIButton *) [cell.contentView viewWithTag:100];
        if (self.isFindButton) {
            findButton.enabled = YES;
            findButton.backgroundColor = UICOLOR(182, 0, 12, 1.0);
            
        }else{
            findButton.enabled = NO;
            findButton.backgroundColor = [UIColor grayColor];;
            
        }
        return cell;
        
    }  //预览
    else if (indexPath.row ==3){   //优惠券说明
        static NSString *identifier = @"N_couponSet_3";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 160)];
            backView.backgroundColor = [UIColor whiteColor];
            [cell.contentView addSubview:backView];
            
            
            UILabel *label1  = [[UILabel alloc]initWithFrame:CGRectMake(15, 0,80, 35)];
            label1.backgroundColor = [UIColor clearColor];
            label1.font = [UIFont systemFontOfSize:13.f];
            label1.text = @"优惠券说明";
            [backView addSubview:label1];
            
            
            UITextView *textView = [[UITextView alloc]initWithFrame:CGRectMake(25, 35, APP_VIEW_WIDTH-50, 110)];
            textView.backgroundColor = [UIColor clearColor];
            textView.font = [UIFont systemFontOfSize:13.f];
            textView.tag = 7788;
            textView.delegate = self;
            textView.showsHorizontalScrollIndicator = NO;
            textView.scrollEnabled = YES;
            textView.returnKeyType = UIReturnKeyDone;
            [backView addSubview:textView];
            
            textView.layer.borderColor = [[UIColor grayColor]CGColor];
            textView.layer.borderWidth = 0.3;
            textView.layer.cornerRadius = 4;
            textView.layer.masksToBounds = YES;
            
        }
        
        UITextView *textView = (UITextView *)[cell.contentView viewWithTag:7788];
        
        textView.text = self.remark;
        
        
        return cell;
    }   //说明
    else if (indexPath.row ==2){
        
        static NSString *identifier = @"N_couponSet_2";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor clearColor];
            UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, SubCellHeight*2)];
            backView.backgroundColor = [UIColor whiteColor];
            [cell.contentView addSubview:backView];
            
        
            UIButton *button1 = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 20, 20)];
            [button1 setImage:[UIImage imageNamed:@"radio_no"] forState:UIControlStateNormal];
            [button1 setImage:[UIImage imageNamed:@"radio_yes"] forState:UIControlStateSelected];
            button1.tag = 100;
            [button1 addTarget:self action:@selector(seleOtherOne:) forControlEvents:UIControlEventTouchUpInside];
            button1.center = CGPointMake(20, 35/2);
            [backView addSubview:button1];
            
            UILabel *label1  = [[UILabel alloc]initWithFrame:CGRectMake(40, 0, 80, SubCellHeight)];
            label1.backgroundColor = [UIColor clearColor];
            label1.font = [UIFont systemFontOfSize:13.f];
            label1.text = @"领取日期:";
            [backView addSubview:label1];
            
            UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, 34.5, APP_VIEW_WIDTH, 0.5)];
            lineView.backgroundColor = self.view.backgroundColor;
            [backView addSubview:lineView];
            
            UILabel *label11  = [[UILabel alloc]initWithFrame:CGRectMake(30, 35,20, 35)];
            label11.backgroundColor = [UIColor whiteColor];
            label11.font = [UIFont systemFontOfSize:13.f];
            label11.text = @"从";
            [backView addSubview:label11];
            
            UILabel *label12  = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 35,20, 35)];
            label12.backgroundColor = [UIColor whiteColor];
            label12.font = [UIFont systemFontOfSize:13.f];
            label12.text = @"到";
            [backView addSubview:label12];
            
            UIButton *dateStartBtn = [[UIButton alloc]initWithFrame:CGRectMake(label11.frame.origin.x+label11.frame.size.width , 35, label12.frame.origin.x-label11.frame.size.width-30, 35)];
            [dateStartBtn setTitle:@"0000-00-00" forState:UIControlStateNormal];
            dateStartBtn.backgroundColor = [UIColor clearColor];
            dateStartBtn.titleLabel.font = [UIFont systemFontOfSize:12.f];
            [dateStartBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            [dateStartBtn addTarget:self action:@selector(seleDate:) forControlEvents:UIControlEventTouchUpInside];
            dateStartBtn.tag = 3999;
            [backView addSubview:dateStartBtn];
            
            UIButton *dateEndButton = [[UIButton alloc]initWithFrame:CGRectMake(label12.frame.origin.x+20 , 35, label12.frame.origin.x-label11.frame.size.width-30, 35)];
            [dateEndButton setTitle:@"0000-00-00" forState:UIControlStateNormal];
            dateEndButton.backgroundColor = [UIColor clearColor];
            dateEndButton.titleLabel.font = [UIFont systemFontOfSize:12.f];
            [dateEndButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            [dateEndButton addTarget:self action:@selector(seleDate:) forControlEvents:UIControlEventTouchUpInside];
            dateEndButton.tag = 4000;
            [backView addSubview:dateEndButton];
            
            
            UIView *lineView2 = [[UIView alloc]initWithFrame:CGRectMake(0, 69.5, APP_VIEW_WIDTH, 0.5)];
            lineView2.backgroundColor = self.view.backgroundColor;
            [backView addSubview:lineView2];
            
            UIView *backView2 = [[UIView alloc] initWithFrame:CGRectMake(0, 5+SubCellHeight*2, APP_VIEW_WIDTH, SubCellHeight*3)];
            backView2.backgroundColor = backView.backgroundColor;
            [cell.contentView addSubview:backView2];
            
            
            UIButton *button2 = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 20, 20)];
            [button2 setImage:[UIImage imageNamed:@"radio_no"] forState:UIControlStateNormal];
            [button2 setImage:[UIImage imageNamed:@"radio_yes"] forState:UIControlStateSelected];
            button2.tag = 200;
            button2.center = CGPointMake(20, 35/2);
            [button2 addTarget:self action:@selector(seleOtherOne:) forControlEvents:UIControlEventTouchUpInside];
            [backView2 addSubview:button2];
            
            
            UILabel *label21  = [[UILabel alloc]initWithFrame:CGRectMake(40,0,80, 35)];
            label21.backgroundColor = [UIColor clearColor];
            label21.font = [UIFont systemFontOfSize:13.f];
            label21.text = @"满就送";
            [backView2 addSubview:label21];
            
            
            UIView *lineView3 = [[UIView alloc]initWithFrame:CGRectMake(0, 34.5, APP_VIEW_WIDTH, 0.5)];
            lineView3.backgroundColor = self.view.backgroundColor;
            [backView2 addSubview:lineView3];
            
            
            
            UILabel *label31  = [[UILabel alloc]initWithFrame:CGRectMake(40,35,80, 35)];
            label31.backgroundColor = [UIColor clearColor];
            label31.font = [UIFont systemFontOfSize:13.f];
            label31.text = @"每满";
            [backView2 addSubview:label31];
            
            UILabel *label32  = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100,35,80, 35)];
            label32.backgroundColor = [UIColor clearColor];
            label32.font = [UIFont systemFontOfSize:13.f];
            label32.text = @"送一张优惠券";
            [backView2 addSubview:label32];
            
            
            UITextField *textField1 = [[UITextField alloc]initWithFrame:CGRectMake(label31.frame.origin.x+label31.frame.size.width, 35, label32.frame.origin.x-(label31.frame.origin.x+label31.frame.size.width), 35)];
            textField1.backgroundColor = [UIColor whiteColor];
            textField1.font = [UIFont systemFontOfSize:13.f];
            textField1.placeholder = @"请输入金额";
            textField1.tag = 99;
            textField1.delegate = self;
            textField1.keyboardType=UIKeyboardTypeDecimalPad;
            [textField1 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
            [backView2 addSubview:textField1];
            
            UILabel *label33 = [[UILabel alloc ] initWithFrame:CGRectMake(40,35*2,100, 35)];
            label33.backgroundColor = [UIColor clearColor];
            label33.font = [UIFont systemFontOfSize:13.f];
            label33.text = @"单笔消费最多送";
            [backView2 addSubview:label33];
            
            UILabel *label34  = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100,35*2,80, 35)];
            label34.backgroundColor = [UIColor clearColor];
            label34.font = [UIFont systemFontOfSize:13.f];
            label34.text = @"张优惠券";
            [backView2 addSubview:label34];
            
            UITextField *textField2 = [[UITextField alloc]initWithFrame:CGRectMake(label33.frame.origin.x+label33.frame.size.width, SubCellHeight*2, label34.frame.origin.x-(label33.frame.origin.x+label33.frame.size.width), 35)];
            textField2.backgroundColor = [UIColor whiteColor];
            textField2.font = [UIFont systemFontOfSize:13.f];
            textField2.text = @"1";
            textField2.tag = 92;
            textField2.delegate = self;
            textField2.keyboardType=UIKeyboardTypeDecimalPad;
            [textField2 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
            [backView2 addSubview:textField2];
            if (self.type == 8) {
                backView2.hidden = YES;
                button1.hidden = YES;
            }
            
            
        }

        
        
        UIButton *seleSendButton = (UIButton *)[cell.contentView viewWithTag:200];
        UIButton *seleEndTakeButton = (UIButton *)[cell.contentView viewWithTag:100];
        UITextField *textfile = (UITextField *)[cell.contentView viewWithTag:99];
        UITextField *textFile2 = (UITextField *)[cell.contentView viewWithTag:92];//单笔消费最多送多少张优惠券
        UIButton *startRecButton = (UIButton *)[cell.contentView viewWithTag:3999]; //领取开始时间
        UIButton *endRecButton = (UIButton *)[cell.contentView viewWithTag:4000]; //领取结束时间
        
        
        if (self.isSeleSend) {  //选择满就送
            seleSendButton.selected = YES;
            seleEndTakeButton.selected = NO;
            startRecButton.enabled = NO;
            endRecButton.enabled = NO;
            
            [endRecButton setTitle:@"0000-00-00" forState:UIControlStateNormal];
            [endRecButton setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
            
            textfile.enabled = YES;
            textFile2.enabled = YES;

            textfile.text = self.GetConditions;
            textFile2.text = self.limitedSendNbr;
           
            
        }else{           //选择领取日期
            seleSendButton.selected = NO;
            seleEndTakeButton.selected = YES;
            
            endRecButton.enabled = YES;
            [endRecButton setTitle:self.endReceiveDateStr forState:UIControlStateNormal];
            [endRecButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            
            startRecButton.enabled = YES;
            [startRecButton setTitle:self.startReceiveDateStr forState:UIControlStateNormal];
            [endRecButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            
            self.GetConditions = @"";
            textfile.text = self.GetConditions;
            self.limitedSendNbr = @"";
            textFile2.text = @"";
            
            textfile.enabled = NO;
            textFile2.enabled = NO;

        }
        
    
        
        return cell;
        
       
    }
    else if(indexPath.row == 1){   //设置使用时间
        
        if(self.type == 1||self.type ==3 ||self.type ==4 || self.type == 7 || self.type == 8 ){  //@[@"",@"N元购",@"",@"抵扣券",@"折扣券",@"实物券",@"体验券"]
            
            static NSString *identifier = @"N_couponSet_1";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell ==nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                cell.backgroundColor = [UIColor clearColor];
                
                UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, 142)];
                backView.backgroundColor = [UIColor whiteColor];
                [cell.contentView addSubview:backView];
                
                UIButton *button1 = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 15, 15)];
                [button1 setImage:[UIImage imageNamed:@"multiple_nochoice"] forState:UIControlStateNormal];
                [button1 setImage:[UIImage imageNamed:@"multiple_choice"] forState:UIControlStateSelected];
                button1.tag = 100;
                [button1 addTarget:self action:@selector(seleData:) forControlEvents:UIControlEventTouchUpInside];
                button1.center = CGPointMake(20, 35/2);
                [backView addSubview:button1];
                
                
                UILabel *label1  = [[UILabel alloc]initWithFrame:CGRectMake(40, 0,100, 35)];
                label1.backgroundColor = [UIColor whiteColor];
                label1.font = [UIFont systemFontOfSize:13.f];
                label1.text = @"使用日期";
                [backView addSubview:label1];
                
                UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, 35, APP_VIEW_WIDTH, 0.5)];
                lineView.backgroundColor = self.view.backgroundColor;
                [backView addSubview:lineView];
                
                UILabel *label11  = [[UILabel alloc]initWithFrame:CGRectMake(30, 36,20, 35)];
                label11.backgroundColor = [UIColor whiteColor];
                label11.font = [UIFont systemFontOfSize:13.f];
                label11.text = @"从";
                [backView addSubview:label11];
                
                UILabel *label12  = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 36,20, 35)];
                label12.backgroundColor = [UIColor whiteColor];
                label12.font = [UIFont systemFontOfSize:13.f];
                label12.text = @"到";
                [backView addSubview:label12];
                
                UIButton *button11 = [[UIButton alloc]initWithFrame:CGRectMake(label11.frame.origin.x+label11.frame.size.width , 36, label12.frame.origin.x-label11.frame.size.width-30, 35)];
                [button11 setTitle:@"0000-00-00" forState:UIControlStateNormal];
                button11.backgroundColor = [UIColor clearColor];
                button11.titleLabel.font = [UIFont systemFontOfSize:12.f];
                [button11 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                [button11 addTarget:self action:@selector(seleDate:) forControlEvents:UIControlEventTouchUpInside];
                button11.tag = 2000;
                [backView addSubview:button11];
                
                UIButton *button12 = [[UIButton alloc]initWithFrame:CGRectMake(label12.frame.origin.x+20 , 36, label12.frame.origin.x-label11.frame.size.width-30, 35)];
                [button12 setTitle:@"0000-00-00" forState:UIControlStateNormal];
                button12.backgroundColor = [UIColor clearColor];
                button12.titleLabel.font = [UIFont systemFontOfSize:12.f];
                [button12 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                [button12 addTarget:self action:@selector(seleDate:) forControlEvents:UIControlEventTouchUpInside];
                button12.tag = 2222;
                [backView addSubview:button12];
                
                
                
                UIView *lineView1 = [[UIView alloc]initWithFrame:CGRectMake(0, 72, APP_VIEW_WIDTH, 0.5)];
                lineView1.backgroundColor = self.view.backgroundColor;
                [backView addSubview:lineView1];
                
                
                
                
                UIButton *button2 = [[UIButton alloc]initWithFrame:CGRectMake(0, 73, 15, 15)];
                [button2 setImage:[UIImage imageNamed:@"multiple_nochoice"] forState:UIControlStateNormal];
                [button2 setImage:[UIImage imageNamed:@"multiple_choice"] forState:UIControlStateSelected];
                button2.tag = 200;
                [button2 addTarget:self action:@selector(seleData:) forControlEvents:UIControlEventTouchUpInside];
                button2.center = CGPointMake(20, 35/2+73);
                [backView addSubview:button2];
                
                
                UILabel *label21  = [[UILabel alloc]initWithFrame:CGRectMake(40, 73,300, 35)];
                label21.backgroundColor = [UIColor whiteColor];
                label21.font = [UIFont systemFontOfSize:13.f];
                NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:@"每天使用时间 建议填写店铺的冷淡期"];
                
                UIColor *titleColor = [UIColor grayColor];
                UIFont *titleFont = [UIFont boldSystemFontOfSize:11];
                NSDictionary *titleDict = [NSDictionary dictionaryWithObjectsAndKeys:
                                           titleColor, NSForegroundColorAttributeName,
                                           titleFont, NSFontAttributeName,
                                           nil];
                
                [str addAttributes: titleDict  range:NSMakeRange(6,11)];
                label21.attributedText = str;
                [backView addSubview:label21];
                
                UIView *lineView2 = [[UIView alloc]initWithFrame:CGRectMake(0, 108, APP_VIEW_WIDTH, 0.5)];
                lineView2.backgroundColor = self.view.backgroundColor;
                [backView addSubview:lineView2];
                
                
                
                UILabel *label31  = [[UILabel alloc]initWithFrame:CGRectMake(30, 109,20, 35)];
                label31.backgroundColor = [UIColor clearColor];
                label31.font = [UIFont systemFontOfSize:13.f];
                label31.text = @"从";
                [backView addSubview:label31];
                
                UILabel *label22  = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 109,20, 35)];
                label22.backgroundColor = [UIColor clearColor];
                label22.font = [UIFont systemFontOfSize:13.f];
                label22.text = @"到";
                [backView addSubview:label22];
                
                UIButton *button21 = [[UIButton alloc]initWithFrame:CGRectMake(label11.frame.origin.x+label11.frame.size.width , 109, label12.frame.origin.x-label11.frame.size.width-30, 35)];
                [button21 setTitle:@"0000-00-00" forState:UIControlStateNormal];
                button21.backgroundColor = [UIColor clearColor];
                button21.titleLabel.font = [UIFont systemFontOfSize:12.f];
                [button21 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                button21.tag = 3000;
                [button21 addTarget:self action:@selector(seleTime:) forControlEvents:UIControlEventTouchUpInside];
                [backView addSubview:button21];
                
                UIButton *button22 = [[UIButton alloc]initWithFrame:CGRectMake(label12.frame.origin.x+20 , 109, label12.frame.origin.x-label11.frame.size.width-30, 35)];
                [button22 setTitle:@"0000-00-00" forState:UIControlStateNormal];
                button22.backgroundColor = [UIColor clearColor];
                button22.titleLabel.font = [UIFont systemFontOfSize:12.f];
                [button22 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                button22.tag = 3333;
                [button22 addTarget:self action:@selector(seleTime:) forControlEvents:UIControlEventTouchUpInside];
                
                [backView addSubview:button22];
                
                
                
            }
            
            UIButton *useDateButton = (UIButton *)[cell.contentView viewWithTag:100];
            UIButton *useTimeButton = (UIButton *)[cell.contentView viewWithTag:200];
            UIButton *startButton = (UIButton *)[cell.contentView viewWithTag:2000];
            UIButton *endButton = (UIButton *)[cell.contentView viewWithTag:2222];
            
            UIButton *StratTimeButton= (UIButton *)[cell.contentView viewWithTag:3000];
            UIButton *endTimebutton = (UIButton *)[cell.contentView viewWithTag:3333];
            
            
            if (self.isDateButton) {
                useDateButton.selected = YES;
                startButton.enabled = YES;
                endButton.enabled = YES;
                
//                NSDate *date = [NSDate date];
//                self.startDate = date;
//                NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
//                [outputFormatter setDateFormat:@"yyyy-MM-dd"];
//                NSString *timestamp_str = [outputFormatter stringFromDate:date];
//                self.startDateStr = timestamp_str;
//                
//                NSDate *nextDate = [NSDate dateWithTimeInterval:24*60*60*7 sinceDate:date];
//                self.endDate = nextDate;
//                timestamp_str = [outputFormatter stringFromDate:nextDate];
//                self.endDateStr = timestamp_str;
                
                [startButton setTitle:self.startDateStr forState:UIControlStateNormal];
                [startButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                [endButton setTitle:self.endDateStr forState:UIControlStateNormal];
                [endButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                
            }else{
                useDateButton.selected = NO;
                startButton.enabled = NO;
                endButton.enabled = NO;
                [startButton setTitle:@"0000-00-00" forState:UIControlStateNormal];
                [endButton setTitle:@"0000-00-00" forState:UIControlStateNormal];
                self.startDateStr = @"0000-00-00";
                self.endDateStr = @"0000-00-00";
                [startButton setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
                [endButton setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
                
            }
            
            if (self.isTimeButton) {
                useTimeButton.selected = YES;
                StratTimeButton.enabled = YES;
                endTimebutton.enabled = YES;
//                self.startTimeStr = @"09:00";
//                self.endTimeStr = @"18:00";
                [StratTimeButton setTitle:self.startTimeStr forState:UIControlStateNormal];
                [endTimebutton setTitle:self.endTimeStr forState:UIControlStateNormal];
                [StratTimeButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                [endTimebutton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                
                
            }else{
                useTimeButton.selected = NO;
                StratTimeButton.enabled = NO;
                endTimebutton.enabled = NO;
                self.startTimeStr = @"00:00";
                self.endTimeStr = @"24:00";
                [StratTimeButton setTitle:self.startTimeStr forState:UIControlStateNormal];
                [endTimebutton setTitle:self.endTimeStr forState:UIControlStateNormal];
                [StratTimeButton setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
                [endTimebutton setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
                
                
            }
            
            return cell;
            
            
        }
        else{     //实物，体验
            static NSString *identifier = @"56_couponSet_1";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell ==nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                cell.backgroundColor = [UIColor clearColor];
                UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 71)];
                backView.backgroundColor = [UIColor whiteColor];
                [cell.contentView addSubview:backView];
                
                UIButton *button1 = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 20, 20)];
                [button1 setImage:[UIImage imageNamed:@"multiple_nochoice"] forState:UIControlStateNormal];
                [button1 setImage:[UIImage imageNamed:@"multiple_choice"] forState:UIControlStateSelected];
                button1.tag = 200;
                [button1 addTarget:self action:@selector(seleData:) forControlEvents:UIControlEventTouchUpInside];
                button1.center = CGPointMake(20, 35/2);
                [backView addSubview:button1];
                
                
                UILabel *label1  = [[UILabel alloc]initWithFrame:CGRectMake(40, 0,100, 35)];
                label1.backgroundColor = [UIColor whiteColor];
                label1.font = [UIFont systemFontOfSize:13.f];
                label1.text = @"使用日期";
                [backView addSubview:label1];
                
                UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, 35, APP_VIEW_WIDTH, 0.5)];
                lineView.backgroundColor = self.view.backgroundColor;
                [backView addSubview:lineView];
                
                UILabel *label11  = [[UILabel alloc]initWithFrame:CGRectMake(30, 36,20, 35)];
                label11.backgroundColor = [UIColor clearColor];
                label11.font = [UIFont systemFontOfSize:13.f];
                label11.text = @"从";
                [backView addSubview:label11];
                
                UILabel *label12  = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 36,20, 35)];
                label12.backgroundColor = [UIColor clearColor];
                label12.font = [UIFont systemFontOfSize:13.f];
                label12.text = @"到";
                [backView addSubview:label12];
                
                UIButton *button11 = [[UIButton alloc]initWithFrame:CGRectMake(label11.frame.origin.x+label11.frame.size.width , 36, label12.frame.origin.x-label11.frame.size.width-30, 35)];
                [button11 setTitle:@"0000-00-00" forState:UIControlStateNormal];
                button11.backgroundColor = [UIColor clearColor];
                button11.titleLabel.font = [UIFont systemFontOfSize:12.f];
                [button11 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                button11.tag = 2000;
                [button11 addTarget:self action:@selector(seleDate:) forControlEvents:UIControlEventTouchUpInside];
                
                [backView addSubview:button11];
                
                UIButton *button12 = [[UIButton alloc]initWithFrame:CGRectMake(label12.frame.origin.x+20 , 36, label12.frame.origin.x-label11.frame.size.width-30, 35)];
                [button12 setTitle:@"0000-00-00" forState:UIControlStateNormal];
                button12.backgroundColor = [UIColor clearColor];
                button12.titleLabel.font = [UIFont systemFontOfSize:12.f];
                [button12 setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                button12.tag = 2222;
                [button12 addTarget:self action:@selector(seleDate:) forControlEvents:UIControlEventTouchUpInside];
                [backView addSubview:button12];
                
            }
            
            UIButton *useTimeButton = (UIButton *)[cell.contentView viewWithTag:200];
            UIButton *startButton = (UIButton *)[cell.contentView viewWithTag:2000];
            UIButton *endButton = (UIButton *)[cell.contentView viewWithTag:2222];
            
            
            if (self.isTimeButton) {
                useTimeButton.selected = YES;
                startButton.enabled = YES;
                endButton.enabled = YES;
                [startButton setTitle:self.startDateStr forState:UIControlStateNormal];
                [endButton setTitle:self.endDateStr forState:UIControlStateNormal];
                [startButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                [endButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                
                
            }else{
                useTimeButton.selected = NO;
                startButton.enabled = NO;
                endButton.enabled = NO;
                [startButton setTitle:@"0000-00-00" forState:UIControlStateNormal];
                self.startDateStr = @"0000-00-00";
                [endButton setTitle:@"0000-00-00" forState:UIControlStateNormal];
                self.endDateStr = @"0000-00-00";
                [startButton setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
                [endButton setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
                
                
            }
            
            return cell;
            
        }
    }  //优惠券使用时间
    else if (indexPath.row == 0){
        if (self.type ==1) {
            
            static NSString *identifier = @"N_couponSet";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell ==nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                cell.backgroundColor = [UIColor clearColor];
                UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 71)];
                backView.backgroundColor = [UIColor whiteColor];
                [cell.contentView addSubview:backView];
                
                UILabel *label1  = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 60, 35)];
                label1.backgroundColor = [UIColor whiteColor];
                label1.font = [UIFont systemFontOfSize:13.f];
                label1.text = @"每张可以";
                [backView addSubview:label1];
                UITextField *textField1 = [[UITextField alloc]initWithFrame:CGRectMake(label1.frame.origin.x+label1.frame.size.width, 0, APP_VIEW_WIDTH-70, 35)];
                textField1.backgroundColor = [UIColor whiteColor];
                textField1.font = [UIFont systemFontOfSize:13.f];
                textField1.placeholder = @"描述本次优惠可以做什么";
                textField1.tag = TAG_FUNCTION;
                textField1.delegate = self;
                textField1.returnKeyType = UIReturnKeyDone;
                [textField1 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                [backView addSubview:textField1];
                
                UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, 35, APP_VIEW_WIDTH, 0.5)];
                lineView.backgroundColor = self.view.backgroundColor;
                [backView addSubview:lineView];
                
                UILabel *label2  = [[UILabel alloc]initWithFrame:CGRectMake(10, 36, 60, 35)];
                label2.backgroundColor = [UIColor whiteColor];
                label2.text = @"每张面值";
                label2.font = [UIFont systemFontOfSize:13.f];
                [backView addSubview:label2];
                
                UITextField *textField2 = [[UITextField alloc]initWithFrame:CGRectMake(label2.frame.origin.x+label2.frame.size.width, label2.frame.origin.y, APP_VIEW_WIDTH/2-60, 35)];
                textField2.backgroundColor = [UIColor whiteColor];
                textField2.placeholder = @"请输入金额";
                textField2.delegate = self;
                textField2.returnKeyType = UIReturnKeyDone;
                textField2.tag = 1001;
                textField2.keyboardType = UIKeyboardTypeDecimalPad;
                [textField2 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                textField2.textAlignment = NSTextAlignmentCenter;
                textField2.font = [UIFont systemFontOfSize:13.f];
                [backView addSubview:textField2];
                
                UILabel *label3  = [[UILabel alloc]initWithFrame:CGRectMake(textField2.frame.origin.x+textField2.frame.size.width, label2.frame.origin.y, 50, 35)];
                label3.backgroundColor = [UIColor whiteColor];
                label3.text = @"元,共发";
                label3.font = [UIFont systemFontOfSize:13.f];
                [backView addSubview:label3];
                
                UILabel *label4  = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-25, label2.frame.origin.y,20, 35)];
                label4.backgroundColor = [UIColor whiteColor];
                label4.font = [UIFont systemFontOfSize:13.f];
                label4.text = @"张";
                [backView addSubview:label4];
                
                UITextField *textField3 = [[UITextField alloc]initWithFrame:CGRectMake(label3.frame.origin.x+label3.frame.size.width,label2.frame.origin.y, APP_VIEW_WIDTH-label3.frame.origin.x-label3.frame.size.width-30, 35)];
                textField3.backgroundColor = [UIColor whiteColor];
                textField3.placeholder = @"请输入张数";
                textField3.font  = [UIFont systemFontOfSize:13.f];
                textField3.delegate = self;
                textField3.returnKeyType = UIReturnKeyDone;
                textField3.tag = 1002;
                textField3.keyboardType = UIKeyboardTypeDecimalPad;
                [textField3 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                textField3.textAlignment = NSTextAlignmentCenter;
                [backView addSubview:textField3];
                
            }
            
            UITextField *deText = (UITextField *)[cell.contentView viewWithTag:TAG_FUNCTION];
            deText.text = self.Desctription;
            
            UITextField *valueText = (UITextField *)[cell.contentView viewWithTag:1001];
            valueText.text = self.Value;
            
            UITextField *counText = (UITextField *)[cell.contentView viewWithTag:1002];
            counText.text = self.totalVolume;
            
            return cell;
            
            
        }
        
        else if (self.type ==3 || self.type ==4){
            
            static NSString *identifier = @"34_couponSet";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell ==nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                cell.backgroundColor = [UIColor clearColor];
                
                UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 35*3 + 45)];
                backView.backgroundColor = [UIColor whiteColor];
                [cell.contentView addSubview:backView];

                CGFloat LabelY = 0;
                UILabel *label01  = [[UILabel alloc]initWithFrame:CGRectMake(10, LabelY, 60, 35)];
                label01.backgroundColor = [UIColor whiteColor];
                label01.text = @"每张可以";
                label01.font = [UIFont systemFontOfSize:13.f];
//                label01.tag = 100;
                [backView addSubview:label01];
                
                UITextField *textField0 = [[UITextField alloc]initWithFrame:CGRectMake(label01.frame.origin.x+label01.frame.size.width, LabelY, APP_VIEW_WIDTH-70, 35)];
                textField0.backgroundColor = [UIColor whiteColor];
                textField0.font = [UIFont systemFontOfSize:13.f];
                textField0.placeholder = @"描述本次优惠可以做什么";
                textField0.tag = TAG_FUNCTION;
                textField0.delegate = self;
                textField0.returnKeyType = UIReturnKeyDone;
                [textField0 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                [backView addSubview:textField0];
                
                UIView *line0 = [[UIView alloc] initWithFrame:CGRectMake(0, 35-0.5, APP_VIEW_WIDTH, 0.5)];
                line0.backgroundColor = self.view.backgroundColor;
                [backView addSubview:line0];
                
                
                LabelY = LabelY + 35;
                
                UILabel *label11  = [[UILabel alloc]initWithFrame:CGRectMake(10, LabelY, 60, 35)];
                label11.backgroundColor = [UIColor whiteColor];
                label11.text = @"每张减免";
                label11.font = [UIFont systemFontOfSize:13.f];
                label11.tag = 100;
                [backView addSubview:label11];
                
                UITextField *textField1 = [[UITextField alloc]initWithFrame:CGRectMake(label11.frame.origin.x+label11.frame.size.width, LabelY, APP_VIEW_WIDTH-130, 35)];
                textField1.backgroundColor = [UIColor clearColor];
                textField1.placeholder = @"请输入金额";
                textField1.textAlignment = NSTextAlignmentCenter;
                textField1.font = [UIFont systemFontOfSize:13.f];
                textField1.delegate = self;
                textField1.returnKeyType = UIReturnKeyDone;
                textField1.tag = TAG_INSTEAD_PRICE;
                textField1.keyboardType = UIKeyboardTypeDecimalPad;
                [textField1 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                [backView addSubview:textField1];
                
                UILabel *label12  = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-60, LabelY, 50, SubCellHeight)];
                label12.textAlignment = NSTextAlignmentRight;
                label12.backgroundColor = [UIColor whiteColor];
                label12.text = @"元";
                label12.tag = 101;
                label12.font = [UIFont systemFontOfSize:13.f];
                [backView addSubview:label12];
                
                
                LabelY = LabelY + 35;
                
                UIView *lineView1 = [[UIView alloc]initWithFrame:CGRectMake(0, LabelY-0.5, APP_VIEW_WIDTH,0.5)];
                lineView1.backgroundColor = self.view.backgroundColor;
                [backView addSubview:lineView1];
                
                UIButton *btn2 = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 15, 15)];
                [btn2 setImage:[UIImage imageNamed:@"multiple_nochoice"] forState:UIControlStateNormal];
                [btn2 setImage:[UIImage imageNamed:@"multiple_choice"] forState:UIControlStateSelected];
                [btn2 addTarget:self action:@selector(seleTop:) forControlEvents:UIControlEventTouchUpInside];
                btn2.center = CGPointMake(20, LabelY + 45/2/2);
                btn2.tag = 1702;
                [backView addSubview:btn2];
                
                UILabel *label21 = [[UILabel alloc] initWithFrame:CGRectMake(40, LabelY, 50, 45/2)];
                label21.font = [UIFont systemFontOfSize:13.f];
                label21.backgroundColor = [UIColor clearColor];
                label21.text = @"   发行";
                [backView addSubview:label21];
                
                UILabel *label22 = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-40, LabelY, 30, 45/2)];
                label22.textAlignment = NSTextAlignmentRight;
                label22.backgroundColor = [UIColor clearColor];
                label22.font = [UIFont systemFontOfSize:13.f];
                label22.text = @"张";
                [backView addSubview:label22];
                
                
                UITextField *textField2 = [[UITextField alloc]initWithFrame:CGRectMake(label21.frame.origin.x+label21.frame.size.width, label21.frame.origin.y,  APP_VIEW_WIDTH-90-30, 45/2)];
                textField2.backgroundColor = [UIColor clearColor];
                textField2.placeholder = @"请输入张数";
                textField2.font  = [UIFont systemFontOfSize:13.f];
                textField2.delegate = self;
                textField2.returnKeyType = UIReturnKeyDone;
                textField2.tag = TAG_TOTAL_VOLUME;
                textField2.keyboardType = UIKeyboardTypeDecimalPad;
                [textField2 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                textField2.textAlignment = NSTextAlignmentCenter;
                [backView addSubview:textField2];
                
                
                UILabel *label20 = [[UILabel alloc] initWithFrame:CGRectMake(40, LabelY+45/2, APP_VIEW_WIDTH-80, 45/2)];
                label20.font = [UIFont systemFontOfSize:12.f];
                label20.text = @"(默认发行数量不限制)";
                label20.textColor = [UIColor lightGrayColor];
                [backView addSubview:label20];
                
                
                
                LabelY = LabelY + 45 ;
                UIView *lineView2 = [[UIView alloc]initWithFrame:CGRectMake(0, LabelY-0.5, APP_VIEW_WIDTH,0.5)];
                lineView2.backgroundColor = self.view.backgroundColor;
                [backView addSubview:lineView2];
                
                
                UILabel *label31  = [[UILabel alloc]initWithFrame:CGRectMake(10, LabelY, 40, 35)];
                label31.backgroundColor = [UIColor whiteColor];
                label31.font = [UIFont systemFontOfSize:13.f];
                NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:@"*  满"];
                [str addAttribute:NSForegroundColorAttributeName value:[UIColor redColor] range:NSMakeRange(0,1)];
                label31.attributedText = str;
                [backView addSubview:label31];
                
                UILabel *label32  = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-180, LabelY, 180, 35)];
                label32.backgroundColor = [UIColor whiteColor];
                label32.font = [UIFont systemFontOfSize:13.f];
                label32.text = @"元可以使用一张优惠券";
                [backView addSubview:label32];
                
                
                
                UITextField *textField3 = [[UITextField alloc]initWithFrame:CGRectMake(label31.frame.origin.x+label31.frame.size.width,LabelY, APP_VIEW_WIDTH-180-50, 35)];
                textField3.backgroundColor = [UIColor clearColor];
                textField3.font = [UIFont systemFontOfSize:13.f];
                textField3.placeholder = @"请输入金额";

                textField3.delegate = self;
                textField3.returnKeyType = UIReturnKeyDone;
                textField3.tag = TAG_AVAILABLE_PRICE;
                textField3.keyboardType = UIKeyboardTypeDecimalPad;
                textField3.textAlignment = NSTextAlignmentCenter;
                [textField3 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                [backView addSubview:textField3];
                
                //4
                LabelY = LabelY +35+15;
                
                
                UIView *backView1 = [[UIView alloc]initWithFrame:CGRectMake(0, LabelY, APP_VIEW_WIDTH, 45+35)];
                backView1.backgroundColor = [UIColor whiteColor];
                [cell.contentView addSubview:backView1];
                
                UIButton *btn4 = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 15, 15)];
                [btn4 setImage:[UIImage imageNamed:@"multiple_nochoice"] forState:UIControlStateNormal];
                [btn4 setImage:[UIImage imageNamed:@"multiple_choice"] forState:UIControlStateSelected];
                btn4.center = CGPointMake(20, 45/2/2);
                btn4.tag = 1704;
                [backView1 addSubview:btn4];
                [btn4 addTarget:self action:@selector(seleTop:) forControlEvents:UIControlEventTouchUpInside];
                
                UILabel *label41 = [[UILabel alloc]initWithFrame:CGRectMake(40, 0, APP_VIEW_WIDTH, 45/2)];
                label41.text = @"   单人最多领取                          张优惠券";
                label41.backgroundColor = [UIColor clearColor];
                label41.font = [UIFont systemFontOfSize:13.f];
                [backView1 addSubview:label41];
                
                UITextField *textfild4 = [[UITextField alloc]initWithFrame:CGRectMake(130, 0, 90, 45/2)];
                textfild4.tag = TAG_NBR_PER_PERSON;
                textfild4.backgroundColor = [UIColor clearColor];
                textfild4.placeholder = @"请输入张数";
                textfild4.keyboardType = UIKeyboardTypeNumberPad;
                textfild4.textAlignment = NSTextAlignmentCenter;
                textfild4.font = [UIFont systemFontOfSize:13.f];
                [backView1 addSubview:textfild4];
                
                
                UILabel *label40 = [[UILabel alloc] initWithFrame:CGRectMake(40, 45/2, APP_VIEW_WIDTH-80, 45/2)];
                label40.font = [UIFont systemFontOfSize:12.f];
                label40.text = @"(默认单人领取/购买数量不限制)";
                label40.textColor = [UIColor lightGrayColor];
                [backView1 addSubview:label40];
                
                
                UIView *line4 = [[UIView alloc] initWithFrame:CGRectMake(0, 45-0.5, APP_VIEW_WIDTH, 0.5)];
                line4.backgroundColor = self.view.backgroundColor;
                [backView1 addSubview:line4];
                
                
                UILabel *label5 = [[UILabel alloc]initWithFrame:CGRectMake(10, 45, APP_VIEW_WIDTH, 35)];
                str = [[NSMutableAttributedString alloc] initWithString:@"*  单笔消费最多使用                          张优惠券"];
                label5.tag = 105;
                [str  addAttribute:NSForegroundColorAttributeName value:[UIColor redColor] range:NSMakeRange(0,3)];
                label5.attributedText = str;
                label5.font = [UIFont systemFontOfSize:13.f];
                label5.backgroundColor = [UIColor clearColor];
                [backView1 addSubview:label5];
                
                
                UITextField *textfild5 = [[UITextField alloc]initWithFrame:CGRectMake(130, 45, 90, 35)];
                textfild5.tag = TAG_LIMITEDNBR;
                textfild5.backgroundColor = [UIColor clearColor];
                textfild5.placeholder = @"请输入张数";
                textfild5.textAlignment = NSTextAlignmentCenter;
                textfild5.keyboardType = UIKeyboardTypeNumberPad;
                textfild5.font = [UIFont systemFontOfSize:13.f];
                
                [textfild4 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                [textfild5 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];


                [backView1 addSubview:textfild4];
                [backView1 addSubview:textfild5];

                
            }
            UIButton *button2 = [cell.contentView viewWithTag:1702];
            UIButton *button4 = [cell.contentView viewWithTag:1704];
            
            
            UILabel *label1 = (UILabel *)[cell.contentView viewWithTag:100];
            UILabel *label2 = (UILabel *)[cell.contentView viewWithTag:101];
            UILabel *label5 = (UILabel *)[cell.contentView viewWithTag:105];
            UITextField *textFiled0 = (UITextField *)[cell.contentView viewWithTag:TAG_FUNCTION];
            UITextField *textFiled1 = (UITextField *)[cell.contentView viewWithTag:TAG_INSTEAD_PRICE];
            UITextField *textFiled2 = (UITextField *)[cell.contentView viewWithTag:TAG_TOTAL_VOLUME];
            UITextField *textFiled3 = (UITextField *)[cell.contentView viewWithTag:TAG_AVAILABLE_PRICE];
            UITextField *textFiled4 = (UITextField *)[cell.contentView viewWithTag:TAG_NBR_PER_PERSON];//单人最多领取 nbrPerPerson
            UITextField *textFiled5 = (UITextField *)[cell.contentView viewWithTag:TAG_LIMITEDNBR];//单笔消费最多使用 limitedNbr


            
            
            
            if (self.type == 3) {
                label1.text = @"每张减免";
                label2.text = @"元";
                textFiled1.placeholder = @"请输入金额";
            }else{
                self.limitedNbr = @"1";
                textFiled5.hidden = YES;
                label1.text = @"每张享受";
                label2.text = @"折";
                textFiled1.placeholder = @"请输入折扣";
                label5.text = @"    单笔消费最多使用             1            张优惠券";

            }
            
            textFiled0.text = self.Desctription;
            textFiled1.text = self.disCount;
            textFiled3.text = self.Value;
            textFiled5.text = self.limitedNbr;
            
            if (self.isTotal) {
                textFiled2.enabled = YES;
                button2.selected = YES;
                textFiled2.text = self.totalVolume;
                
            }else  {
                textFiled2.text = @"";
                textFiled2.enabled = NO;
                button2.selected = NO;
                self.totalVolume = textFiled2.text;
            
            }
            
            if (self.isnbrPerPerson ) {
                button4.selected = YES;
                textFiled4.enabled = YES;
                textFiled4.text = self.nbrPerPerson;
                
            }else {
                textFiled4.text = @"";
                textFiled4.enabled = NO;
                button4.selected = NO;
                self.nbrPerPerson = textFiled4.text;
                
            }
            
            
            
            return cell;
            
        }
        
        else if (self.type ==5 ||self.type ==6){
            static NSString *identifier = @"56_couponSet";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell ==nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                cell.backgroundColor = [UIColor clearColor];
                UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 71)];
                backView.backgroundColor = [UIColor whiteColor];
                [cell.contentView addSubview:backView];
                
                UILabel *label1  = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 60, 35)];
                label1.backgroundColor = [UIColor whiteColor];
                label1.font = [UIFont systemFontOfSize:13.f];
                label1.text = @"每张可以";
                [backView addSubview:label1];
                UITextField *textField1 = [[UITextField alloc]initWithFrame:CGRectMake(label1.frame.origin.x+label1.frame.size.width, 0, APP_VIEW_WIDTH-70, 35)];
                textField1.backgroundColor = [UIColor whiteColor];
                textField1.font = [UIFont systemFontOfSize:13.f];
                textField1.placeholder = @"描述本次优惠可以做什么";
                textField1.tag = TAG_FUNCTION;
                textField1.delegate = self;
                textField1.returnKeyType = UIReturnKeyDone;
                [textField1 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                [backView addSubview:textField1];
                
                UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, 35, APP_VIEW_WIDTH, 0.5)];
                lineView.backgroundColor = self.view.backgroundColor;
                [backView addSubview:lineView];
                
                UILabel *label2  = [[UILabel alloc]initWithFrame:CGRectMake(10, 36, 40, 35)];
                label2.backgroundColor = [UIColor whiteColor];
                label2.text = @"共发";
                label2.font = [UIFont systemFontOfSize:13.f];
                [backView addSubview:label2];
                
                UILabel *label4  = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-25, label2.frame.origin.y,20, 35)];
                label4.backgroundColor = [UIColor whiteColor];
                label4.font = [UIFont systemFontOfSize:13.f];
                label4.text = @"张";
                [backView addSubview:label4];
                
                UITextField *textField3 = [[UITextField alloc]initWithFrame:CGRectMake(label2.frame.origin.x+label2.frame.size.width,label2.frame.origin.y, APP_VIEW_WIDTH-50-30, 35)];
                textField3.backgroundColor = [UIColor whiteColor];
                textField3.placeholder = @"请输入张数";
                textField3.font  = [UIFont systemFontOfSize:13.f];
                textField3.delegate = self;
                textField3.returnKeyType = UIReturnKeyDone;
                textField3.tag = 1002;
                textField3.keyboardType = UIKeyboardTypeNumberPad;
                [textField3 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                textField3.textAlignment = NSTextAlignmentCenter;
                [backView addSubview:textField3];
                
            }
            
            UITextField *deText = (UITextField *)[cell.contentView viewWithTag:TAG_FUNCTION];
            deText.text = self.Desctription;
            
            UITextField *valueText = (UITextField *)[cell.contentView viewWithTag:1002];
            valueText.text = self.Value;
            
            
            
            
            
            return cell;
            
        }
        else if (self.type == 7 || self.type == 8) {
            static NSString *identifier = @"7_couponSet";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell ==nil) {

                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                cell.backgroundColor = [UIColor clearColor];
                UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, SubCellHeight*4+20)];
                backView.backgroundColor = [UIColor whiteColor];
                [cell.contentView addSubview:backView];
                
                UILabel *label1  = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 60, 35)];
                label1.backgroundColor = [UIColor whiteColor];
                label1.font = [UIFont systemFontOfSize:13.f];
                label1.text = @"每张可以";
                [backView addSubview:label1];
                
                UITextField *textField1 = [[UITextField alloc]initWithFrame:CGRectMake(label1.frame.origin.x+label1.frame.size.width, 0, APP_VIEW_WIDTH-70, 35)];
                textField1.backgroundColor = [UIColor whiteColor];
                textField1.font = [UIFont systemFontOfSize:13.f];
                textField1.placeholder = @"描述本次优惠可以做什么";
                textField1.tag = TAG_FUNCTION;
                textField1.delegate = self;
                textField1.returnKeyType = UIReturnKeyDone;
                [textField1 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                [backView addSubview:textField1];
                
                UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, SubCellHeight-0.5, APP_VIEW_WIDTH, 0.5)];
                lineView.backgroundColor = self.view.backgroundColor;
                [backView addSubview:lineView];
                
                if (self.type==7) {
                    UILabel *label21  = [[UILabel alloc]initWithFrame:CGRectMake(10, SubCellHeight, 80, 35)];
                    label21.backgroundColor = [UIColor whiteColor];
                    label21.text = @"每张面值";
                    label21.font = [UIFont systemFontOfSize:13.f];
                    [backView addSubview:label21];
                    
                    UILabel *label22  = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-30, SubCellHeight,20, 35)];
                    label22.textAlignment = NSTextAlignmentRight;
                    label22.backgroundColor = [UIColor whiteColor];
                    label22.font = [UIFont systemFontOfSize:13.f];
                    label22.text = @"元";
                    [backView addSubview:label22];
                    
                    UITextField *textField2 = [[UITextField alloc]initWithFrame:CGRectMake(label21.frame.origin.x+label21.frame.size.width,label21.frame.origin.y, APP_VIEW_WIDTH-90-30, 35)];
                    textField2.backgroundColor = [UIColor whiteColor];
                    textField2.placeholder = @"请输入金额";
                    textField2.font  = [UIFont systemFontOfSize:13.f];
                    textField2.delegate = self;
                    textField2.returnKeyType = UIReturnKeyDone;
                    textField2.tag = TAG_INSTEAD_PRICE;
                    textField2.keyboardType = UIKeyboardTypeDecimalPad;
                    [textField2 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                    textField2.textAlignment = NSTextAlignmentCenter;
                    [backView addSubview:textField2];
                } else {
                    UILabel *label21  = [[UILabel alloc]initWithFrame:CGRectMake(10, SubCellHeight, 52, 35)];
                    label21.backgroundColor = [UIColor whiteColor];
                    label21.text = @"每次购买";
                    label21.font = [UIFont systemFontOfSize:13.f];
                    [backView addSubview:label21];
                    
                    
                    UITextField *textField2 = [[UITextField alloc] initWithFrame:CGRectMake(label21.frame.size.width+label21.frame.origin.x, SubCellHeight, APP_VIEW_WIDTH/2 - (label21.frame.size.width+label21.frame.origin.x), SubCellHeight)];
                    textField2.tag = 8000;
                    textField2.placeholder = @"请输入金额";
                    textField2.font  = [UIFont systemFontOfSize:13.f];
                    textField2.delegate = self;
                    textField2.returnKeyType = UIReturnKeyDone;
 
                    textField2.keyboardType = UIKeyboardTypeDecimalPad;
                    [textField2 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                    textField2.textAlignment = NSTextAlignmentCenter;
                    [backView addSubview:textField2];
                    
                    UILabel *label22 = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, SubCellHeight, 50, 35)];
                    label22.backgroundColor = [UIColor clearColor];
                    label22.text = @"元,代替";
                    label22.font = [UIFont systemFontOfSize:13.f];
                    [backView addSubview:label22];
                    
                    
                    UITextField *textField22 = [[UITextField alloc]initWithFrame:CGRectMake(label22.frame.origin.x+label22.frame.size.width,label21.frame.origin.y, APP_VIEW_WIDTH/2-50-30, 35)];
                    textField22.backgroundColor = [UIColor whiteColor];
                    textField22.placeholder = @"请输入金额";
                    textField22.font  = [UIFont systemFontOfSize:13.f];
                    textField22.delegate = self;
                    textField22.returnKeyType = UIReturnKeyDone;
                    textField22.tag = TAG_INSTEAD_PRICE;
                    textField22.keyboardType = UIKeyboardTypeDecimalPad;
                    [textField22 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                    textField22.textAlignment = NSTextAlignmentCenter;
                    [backView addSubview:textField22];
                    
                    UILabel *label23 = [[UILabel alloc] initWithFrame:CGRectMake(0, SubCellHeight, APP_VIEW_WIDTH-10, SubCellHeight)];
                    label23.text = @"元";
                    label23.textAlignment = NSTextAlignmentRight;
                    label23.font = [UIFont systemFontOfSize:13.f];
                    [backView addSubview:label23];

                    
                    
                }
                
                
                UIView *line2 = [[UIView alloc ]initWithFrame:CGRectMake(0, SubCellHeight*2-0.5, APP_VIEW_WIDTH, 0.5)];
                line2.backgroundColor = APP_VIEW_BACKCOLOR;
                [backView addSubview:line2];
                
                
                UIButton *btn3 = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 15, 15)];
                [btn3 setImage:[UIImage imageNamed:@"multiple_nochoice"] forState:UIControlStateNormal];
                [btn3 setImage:[UIImage imageNamed:@"multiple_choice"] forState:UIControlStateSelected];
                btn3.center = CGPointMake(20, 35*2 + 45/2/2);
                btn3.tag = 1702;
                [btn3 addTarget:self action:@selector(seleTop:) forControlEvents:UIControlEventTouchUpInside];
                [backView addSubview:btn3];
                
                UILabel *label31 = [[UILabel alloc] initWithFrame:CGRectMake(35, SubCellHeight*2, 50, 45/2)];
                label31.font = [UIFont systemFontOfSize:13.f];
                label31.backgroundColor = [UIColor clearColor];
                label31.text = @"发行";
                [backView addSubview:label31];
                
                UILabel *label32 = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-40, SubCellHeight*2, 30, 45/2)];
                label32.textAlignment = NSTextAlignmentRight;
                label32.backgroundColor = [UIColor clearColor];
                label32.font = [UIFont systemFontOfSize:13.f];
                label32.text = @"张";
                [backView addSubview:label32];
                
                UITextField *textField3 = [[UITextField alloc]initWithFrame:CGRectMake(label31.frame.origin.x+label31.frame.size.width, label31.frame.origin.y,  APP_VIEW_WIDTH-90-30, 45/2)];
                textField3.backgroundColor = [UIColor whiteColor];
                textField3.placeholder = @"请输入张数";
                textField3.font  = [UIFont systemFontOfSize:13.f];
                textField3.delegate = self;
                textField3.returnKeyType = UIReturnKeyDone;
                textField3.tag = TAG_TOTAL_VOLUME;
                textField3.keyboardType = UIKeyboardTypeNumberPad;
                [textField3 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                textField3.textAlignment = NSTextAlignmentCenter;
                [backView addSubview:textField3];
                
                UILabel *label30 = [[UILabel alloc] initWithFrame:CGRectMake(40, SubCellHeight*2+45/2, APP_VIEW_WIDTH-80, 45/2)];
                label30.font = [UIFont systemFontOfSize:12.f];
                label30.text = @"(默认发行数量不限制)";
                label30.textColor = [UIColor lightGrayColor];
                [backView addSubview:label30];
                
                
                UIButton *btn4 = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 15, 15)];
                [btn4 setImage:[UIImage imageNamed:@"multiple_nochoice"] forState:UIControlStateNormal];
                [btn4 setImage:[UIImage imageNamed:@"multiple_choice"] forState:UIControlStateSelected];
                btn4.center = CGPointMake(20, 35*2 + 45 + 45/2/2);
                btn4.tag = 1704;
                [btn4 addTarget:self action:@selector(seleTop:) forControlEvents:UIControlEventTouchUpInside];
                [backView addSubview:btn4];
                
                
                UIView *line3 = [[UIView alloc] initWithFrame:CGRectMake(0, SubCellHeight*2+45-0.5, APP_VIEW_WIDTH, 0.5)];
                line3.backgroundColor = APP_VIEW_BACKCOLOR;
                [backView addSubview:line3];
                
                
                
                UILabel *label41 = [[UILabel alloc] initWithFrame:CGRectMake(35, SubCellHeight*2+45, 120, 45/2)];
                label41.font = [UIFont systemFontOfSize:13.f];
                label41.backgroundColor = [UIColor clearColor];
                label41.text = @"单人最多领取/购买";
                [backView addSubview:label41];
                
                UILabel *label42 = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-90, SubCellHeight*2+45, 80, 45/2)];
                label42.textAlignment = NSTextAlignmentRight;
                label42.backgroundColor = [UIColor clearColor];
                label42.font = [UIFont systemFontOfSize:13.f];
                label42.text = @"张优惠券";
                [backView addSubview:label42];
                
                UITextField *textField4 = [[UITextField alloc]initWithFrame:CGRectMake(label41.frame.origin.x+label41.frame.size.width, label41.frame.origin.y,  APP_VIEW_WIDTH-(label41.frame.origin.x+label41.frame.size.width)-90, 45/2)];
                textField4.backgroundColor = [UIColor whiteColor];
                textField4.placeholder = @"请输入张数";
                textField4.font  = [UIFont systemFontOfSize:13.f];
                textField4.delegate = self;
                textField4.returnKeyType = UIReturnKeyDone;
                textField4.tag = TAG_NBR_PER_PERSON;
                textField4.keyboardType = UIKeyboardTypeNumberPad;
                [textField4 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
                textField4.textAlignment = NSTextAlignmentCenter;
                [backView addSubview:textField4];
                
                UILabel *label40 = [[UILabel alloc] initWithFrame:CGRectMake(40, SubCellHeight*2+45+45/2, APP_VIEW_WIDTH-80, 45/2)];
                label40.font = [UIFont systemFontOfSize:12.f];
                label40.text = @"(默认单人领取/购买数量不限制)";
                label40.textColor = [UIColor lightGrayColor];
                [backView addSubview:label40];
                

            }
            UIButton *button2 = [cell.contentView viewWithTag:1702];
            UIButton *button4 = [cell.contentView viewWithTag:1704];
            

            UITextField *textField0 = (UITextField *)[cell.contentView viewWithTag:TAG_FUNCTION];
            UITextField *textField1 = (UITextField *)[cell.contentView viewWithTag:TAG_INSTEAD_PRICE];
            UITextField *textField2 = (UITextField *)[cell.contentView viewWithTag:TAG_TOTAL_VOLUME];
            UITextField *textField3 = (UITextField *)[cell.contentView viewWithTag:TAG_NBR_PER_PERSON];//单人最多领取
            UITextField *textField22 = (UITextField *)[cell.contentView viewWithTag:8000];
            
            textField0.text = self.Desctription;
            textField1.text = self.disCount;
            
            if (self.type == 7) {
                textField0.placeholder = @"本次可以兑换";
            }

            
            
            
            if (self.isTotal) {
                textField2.enabled = YES;
                button2.selected = YES;
                textField2.text = self.totalVolume;
                
            }else  {
                textField2.text = @"";
                textField2.enabled = NO;
                button2.selected = NO;
                self.totalVolume = textField2.text;
                
            }
            
            if (self.isnbrPerPerson ) {
                button4.selected = YES;
                textField3.enabled = YES;
                textField3.text = self.nbrPerPerson;
                
            }else {
                textField3.text = @"";
                textField3.enabled = NO;
                button4.selected = NO;
                self.nbrPerPerson = textField3.text;
                
            }
            
            if (self.type == 8) {
                textField22.text = self.payPrice;

            }
            
            
            return cell;

            
        }//兑换券
        
        
    }
    


    static NSString *identifier = @"aaa";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell ==nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.backgroundColor = [UIColor clearColor];
        UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 80)];
        backView.backgroundColor = [UIColor whiteColor];
        [cell.contentView addSubview:backView];
    }
    return cell;
    
}

-(void)seleTime:(UIButton *)button{
    
    if (button.tag == 3000) {
        self.isStartTime = YES;
        self.seleTimeStr = self.startTimeStr;
        self.isEndTime = NO;
        
    }else if (button.tag == 3333){
        self.isStartTime = NO;
        self.isEndTime = YES;
        self.seleTimeStr = self.endTimeStr;

    }
    
    if (self.seleTimePickerView == nil) {
        self.seleTimePickerView = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2-100, APP_VIEW_HEIGHT/2-90, 280, 200)];
        self.seleTimePickerView.backgroundColor = [UIColor whiteColor];
        self.seleTimePickerView.layer.borderWidth = 0.5;
        self.seleTimePickerView.layer.borderColor = [[UIColor grayColor]CGColor];
        self.seleTimePickerView.layer.cornerRadius = 0.3;
        self.seleTimePickerView.layer.masksToBounds = YES;
        self.seleTimePickerView.center = self.view.center;
        [self.view addSubview:self.seleTimePickerView];
        
        self.seleTimeLable = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 280, 50)];
        self.seleTimeLable.font = [UIFont systemFontOfSize:13.f];
        self.seleTimeLable.textColor = UICOLOR(182, 0, 12, 1.0);
        self.seleTimeLable.text = @"      选择日期(时间)为:";
        [self.seleTimePickerView addSubview:self.seleTimeLable];
        
        UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(240,0 ,40 ,40)];
        button.backgroundColor = [UIColor clearColor];
        [button setTitle:@"关闭" forState:UIControlStateNormal];
        button.titleLabel.font = [UIFont systemFontOfSize:13.f];
        [button setTitleColor:UICOLOR(182, 0, 12, 1.0) forState:UIControlStateNormal];
        [self.seleTimePickerView addSubview:button];
        [button addTarget:self action:@selector(closeTimePicker) forControlEvents:UIControlEventTouchUpInside];
        
        
    }else{
        self.seleTimePickerView.hidden = NO;
    }
    UIDatePicker *datePicker = [[UIDatePicker alloc] initWithFrame:CGRectMake(0, 50, 280, 150)];
    datePicker.backgroundColor = [UIColor whiteColor];
    datePicker.layer.borderWidth = 0.5;
    datePicker.layer.borderColor = [[UIColor grayColor]CGColor];
    datePicker.layer.cornerRadius = 0.3;
    datePicker.layer.masksToBounds = YES;    // 设置时区
    // 设置UIDatePicker的显示模式
    [datePicker setDatePickerMode:UIDatePickerModeTime];
    // 当值发生改变的时候调用的方法
    [datePicker addTarget:self action:@selector(dateTimePickerValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.seleTimePickerView addSubview:datePicker];
  
}
-(void)closeTimePicker{
    self.seleTimePickerView.hidden = YES;
    
    if (self.isStartTime) {
        self.startTimeStr = self.seleTimeStr;
    }else if (self.isEndTime){
        self.endTimeStr  = self.seleTimeStr;
    }
    [[NSNotificationCenter defaultCenter]postNotificationName:@"compareTimeSuecc" object:nil];
    
    
    
    [self.myTableView reloadData];
    

}
-(void)dateTimePickerValueChanged:(UIDatePicker *)picker{
    NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
    NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [outputFormatter setTimeZone:timeZone];
    [outputFormatter setDateFormat:@"HH:mm"];
    NSString *timestamp_str = [outputFormatter stringFromDate:picker.date];
    self.seleTimeStr = timestamp_str;
    self.seleTimeLable.text = [NSString stringWithFormat:@"      选择日期(时间)为:%@", timestamp_str];
    
    
}



-(void)seleDate:(UIButton *)button{
    
    
    if (button.tag == 2000) {
        self.isStartDate = YES;
        self.seleDateStr = self.startDateStr;
        self.isEndDate = NO;
        self.isEndReceDate = NO;
        self.isStartReceDate = NO;
        
    }else if (button.tag == 2222){
        self.isStartDate = NO;
        self.isStartReceDate = NO;
        self.isEndReceDate = NO;
        self.isEndDate = YES;
        self.seleDateStr = self.endDateStr;

        
    }else if (button.tag == 3999) {//领取开始时间
        self.isStartReceDate = YES;
        self.isEndReceDate = NO;
        self.isStartDate = NO;
        self.isEndDate = NO;
        self.seleDateStr = self.startReceiveDateStr;
        
    }else if (button.tag == 4000) {//领取结束时间
        self.isEndReceDate = YES;
        self.isStartReceDate = NO;
        self.isStartDate = NO;
        self.isEndDate = NO;
        self.seleDateStr = self.endReceiveDateStr;

    }
    
    if (self.selePickerView == nil) {
        self.selePickerView = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2-100, APP_VIEW_HEIGHT/2-90, 280, 200)];
        self.selePickerView.backgroundColor = [UIColor whiteColor];
        self.selePickerView.layer.borderWidth = 0.5;
        self.selePickerView.layer.borderColor = [[UIColor grayColor]CGColor];
        self.selePickerView.layer.cornerRadius = 0.3;
        self.selePickerView.layer.masksToBounds = YES;
        self.selePickerView.center = self.view.center;
        [self.view addSubview:self.selePickerView];
        
        self.seleDateLable = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 280, 50)];
        self.seleDateLable.font = [UIFont systemFontOfSize:13.f];
        self.seleDateLable.textColor = UICOLOR(182, 0, 12, 1.0);
        self.seleDateLable.text = @"      选择日期(时间)为:";
        [self.selePickerView addSubview:self.seleDateLable];
        
        UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(240,0 ,40 ,40)];
        button.backgroundColor = [UIColor clearColor];
        [button setTitle:@"关闭" forState:UIControlStateNormal];
        button.titleLabel.font = [UIFont systemFontOfSize:13.f];
        [button setTitleColor:UICOLOR(182, 0, 12, 1.0) forState:UIControlStateNormal];
        [self.selePickerView addSubview:button];
        [button addTarget:self action:@selector(closePicker) forControlEvents:UIControlEventTouchUpInside];
        
        
    }else{
        self.selePickerView.hidden = NO;
    }
    NSDate *tempDate = [NSDate date];
    UIDatePicker *datePicker = [[UIDatePicker alloc] initWithFrame:CGRectMake(0, 50, 280, 150)];
    datePicker.backgroundColor = [UIColor whiteColor];
    datePicker.layer.borderWidth = 0.5;
    datePicker.layer.borderColor = [[UIColor grayColor]CGColor];
    datePicker.layer.cornerRadius = 0.3;
    datePicker.layer.masksToBounds = YES;    // 设置时区
    [datePicker setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
    
    if (button.tag == 2000) {
        // 设置当前显示时间
        [datePicker setDate:tempDate animated:YES];
        // 设置显示最大时间（此处为当前时间）
        [datePicker setMinimumDate:[NSDate date]];
        // 设置UIDatePicker的显示模式
        [datePicker setDatePickerMode:UIDatePickerModeDate];
        
    }else if (button.tag == 2222){
        // 设置当前显示时间
        [datePicker setDate:self.endDate animated:YES];
        // 设置显示最大时间（此处为当前时间）
        [datePicker setMinimumDate:self.startDate];
        // 设置UIDatePicker的显示模式
        [datePicker setDatePickerMode:UIDatePickerModeDate];
        
        
    }else if (button.tag == 3999){
        // 设置当前显示时间
        [datePicker setDate:tempDate animated:YES];
        // 设置显示最大时间（此处为当前时间）
        [datePicker setMinimumDate:tempDate];
        // 设置UIDatePicker的显示模式
        [datePicker setDatePickerMode:UIDatePickerModeDate];
        
        
    }
    else if (button.tag == 4000){
        
        // 设置当前显示时间
        [datePicker setDate:tempDate animated:YES];
        // 设置显示最大时间（此处为当前时间）
        [datePicker setMinimumDate:[NSDate date]];
        // 设置UIDatePicker的显示模式
        [datePicker setDatePickerMode:UIDatePickerModeDate];
        
    }
    
    // 当值发生改变的时候调用的方法
    [datePicker addTarget:self action:@selector(datePickerValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.selePickerView addSubview:datePicker];
}

#pragma mark - 关闭时间选择器 选择时间  年-月-日
-(void)closePicker{
    
    self.selePickerView.hidden = YES;
    
    if (self.isStartDate) {
        self.startDateStr = self.seleDateStr;
    }else if (self.isEndDate){
        self.endDateStr  = self.seleDateStr;
    }
    else if (self.isStartReceDate) {
        self.startReceiveDateStr = self.seleDateStr;
    }
    else if (self.isEndReceDate){
        self.endReceiveDateStr = self.seleDateStr;
    }
    
    [[NSNotificationCenter defaultCenter]postNotificationName:@"compareDateSuecc" object:nil];

    
    [self.myTableView reloadData];
    
}

-(void)datePickerValueChanged:(UIDatePicker *)picker{
    NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
    NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [outputFormatter setTimeZone:timeZone];
    [outputFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *timestamp_str = [outputFormatter stringFromDate:picker.date];
    self.seleDateStr = timestamp_str;
    self.seleDateLable.text = [NSString stringWithFormat:@"      选择日期(时间)为:"];
    if (self.isStartDate) {
        self.startDate = picker.date;
    }else if (self.isEndDate){
        self.endDate = picker.date;
    }
    
}

#pragma mark - UIAlertView delegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)UIAlertView {
    if (UIAlertView == 0) {
        
        
    }else if (UIAlertView == 1) {
        [self gotoFindCouponVC];
        
    }
    
    
}


#pragma mark -- 预览 --
- (void)findCoupon{
    
    if (self.type == 8 && [self.payPrice intValue] == 0) {
        
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"提示"
                                                            message:@"设置0元需谨慎，代金券在消费时当现金使用"
                                                           delegate:self
                                                  cancelButtonTitle:@"现在修改"
                                                  otherButtonTitles:@"继续设置", nil];
        [alertView show];

        
    }else if (self.type == 8 && [self.disCount  intValue] == 0) {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"提示"
                                                            message:@"此优惠券设置的没有意义"
                                                           delegate:self
                                                  cancelButtonTitle:@"现在修改"
                                                  otherButtonTitles: nil];
    }else if ([self.disCount intValue] == 0 ) {
        NSLog(@"%.2f",[self.disCount floatValue]);
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"提示"
                                                            message:@"此优惠券设置的没有意义"
                                                           delegate:self
                                                  cancelButtonTitle:@"现在修改"
                                                  otherButtonTitles: nil];
        [alertView show];
        
    }else if (self.isTotal && self.totalVolume.intValue == 0) {
        NSLog(@"%.2f",[self.disCount floatValue]);
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"提示"
                                                            message:@"优惠券发布张数不能设置为0"
                                                           delegate:self
                                                  cancelButtonTitle:@"现在修改"
                                                  otherButtonTitles: nil];
        [alertView show];
        
        
    }else if(self.disCount.intValue == 0) {
        CSAlert(@"抵扣金额不能设置为0");
    }
//    else if(self)
    
    
    else {
        [self gotoFindCouponVC];
    }
    
}


- (void)gotoFindCouponVC {
    
    
    NSArray *status = @[@"",@"N元购",@"",@"抵扣券",@"折扣券",@"实物券",@"体验券",@"兑换券", @"代金券"];
    BMSQ_FindSetCouponViewController *vc = [[BMSQ_FindSetCouponViewController alloc]init];
    
    if(self.type == 1){
        vc.Desctription = self.Desctription;
        vc.disCount = @"0";
        vc.discountPercent = @"0";
        vc.insteadPrice = [NSString stringWithFormat:@"%@",self.Value];
        vc.availablePrice = @"0";
        vc.function = self.Desctription;
        
    }else if (self.type == 3){
        
        vc.Desctription = [NSString stringWithFormat:@"满%@元减%@元", self.Value, self.disCount];
        vc.disCount = [NSString stringWithFormat:@"￥%@", self.disCount];
        vc.discountPercent = @"0";
        vc.insteadPrice = self.disCount;
        vc.availablePrice = self.Value;
        vc.function = self.Desctription;
        
        
    }else if (self.type == 4){
        vc.Desctription = [NSString stringWithFormat:@"满%@元打%@折",self.Value,self.disCount];
        vc.disCount = [NSString stringWithFormat:@"打%@折",self.disCount];
        vc.discountPercent = self.disCount;
        vc.insteadPrice = @"0";
        vc.availablePrice = self.Value;
        vc.function = self.Desctription;
        
        
    }else if (self.type == 5){
        vc.Desctription = self.Desctription;
        vc.discountPercent = @"0";
        vc.insteadPrice = @"0";
        vc.availablePrice = @"0";
        vc.function = self.Desctription;
        
        
    }else if (self.type ==6){
        vc.Desctription = self.Desctription;
        vc.discountPercent = @"0";
        vc.insteadPrice = @"0";
        vc.availablePrice = @"0";
        vc.function = self.Desctription;
        
        
    }else if (self.type == 7) {
        vc.Desctription = self.Desctription;
        vc.disCount = self.disCount;
        vc.discountPercent = @"0";
        vc.insteadPrice = self.disCount;
        vc.availablePrice = @"0";
        vc.function = self.Desctription;
        
    }else if (self.type == 8) {
        
        
        vc.Desctription = self.Desctription;
        vc.disCount = self.disCount;
        vc.discountPercent = @"0";
        vc.insteadPrice = self.disCount;
        vc.availablePrice = @"0";
        vc.function = self.Desctription;
        vc.payPrice = self.payPrice;
    }
    
    
    
    
    if ([self.startDateStr isEqualToString:@"0000-00-00"]) {
        vc.useDate = @"";
    }else{
        vc.useDate = [NSString stringWithFormat:@"%@ ~ %@",self.startDateStr,self.endDateStr];
    }
    
    if ([self.startTimeStr isEqualToString:@"00:00:00"]) {
        vc.useTime = @"全天使用";
    }else{
        vc.useTime =[NSString stringWithFormat:@"%@ - %@",self.startTimeStr,self.endTimeStr];
    }
    
    if (self.remark.length >0) {
        vc.remark = self.remark;
        
    }else{
        vc.remark = @"      暂无说明";
    }
    
    
    if (self.isTotal) {
        vc.totalVolume = self.totalVolume;
    }else {
        vc.totalVolume = @"-1";
    }
    
    if (self.isnbrPerPerson) {
        vc.nbrPerPerson = self.nbrPerPerson;
    }else {
        vc.nbrPerPerson = @"0";
    }
    
    
    
    vc.type = self.type;
    vc.startUsingTime = self.startDateStr;
    vc.expireTime = self.endDateStr;
    vc.dayStartUsingTime = self.startTimeStr;
    vc.dayEndUsingTime = self.endTimeStr;
    vc.startTakingTime = self.startReceiveDateStr;
    vc.endTakingTime = self.endReceiveDateStr;
    vc.couponTypeName =[status objectAtIndex:self.type];
    
    
    self.isSend = self.isSeleSend?@"1": @"0";
    self.sendRequired = self.isSeleSend?self.GetConditions:@"0";
    
    
    
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    NSString *staffCode = [userDefault objectForKey:@"staffCode"];
    vc.creatorCode = staffCode;
    
    vc.isSend = self.isSend;
    vc.sendRequired = self.sendRequired;
    vc.limitedSendNbr = self.isSeleSend?self.limitedSendNbr:@"0";
    
    
    vc.limitedNbr = self.limitedNbr;
    
    
    
    
    [self.navigationController pushViewController:vc animated:YES];
    

}






-(void)textFieldDidBeginEditing:(UITextField *)textField{
//    if (textField.tag == TAG_FUNCTION || textField.tag == TAG_INSTEAD_PRICE || textField.tag == TAG_TOTAL_VOLUME || textField.tag == TAG_AVAILABLE_PRICE ||) {
        [UIView animateWithDuration:0.2 animations:^{
            self.myTableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-236);
//            NSIndexPath *indexPath = [NSIndexPath indexPathForItem:0 inSection:0];
//            [self.myTableView  scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
        } completion:^(BOOL finished) {
            
        }];
//    }
    
//    if (textField.tag == 99 || textField.tag == 92) {
//        [UIView animateWithDuration:0.2 animations:^{
//            self.myTableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-236);
//            NSIndexPath *indexPath = [NSIndexPath indexPathForItem:2 inSection:0];
//            [self.myTableView  scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
//        } completion:^(BOOL finished) {
//            
//        }];
//    }
    

    
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    [UIView animateWithDuration:0.2 animations:^{
        self.myTableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH,APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
        
    } completion:^(BOOL finished) {
        
    }];
    return YES;
}

-(void)textFieldChange:(UITextField *)textField{
    
    NSLog(@"%ld",(long)textField.tag);
    
    int textLength = 10;
    
    if (textField.tag == TAG_INSTEAD_PRICE) {  //减免多少或者每张面值多少

        if (textField.text.length >=textLength) {
            textField.text = [textField.text substringToIndex:textLength];
        }
        self.disCount = textField.text;
        
        
        
    }else if (textField.tag == TAG_TOTAL_VOLUME){ //发行多少张
        if (textField.text.length >=textLength) {
            textField.text = [textField.text substringToIndex:textLength];
            
        }
        self.totalVolume = textField.text;
        
    }else if (textField.tag == TAG_AVAILABLE_PRICE) {//满多少可以使用

        if (textField.text.length >=textLength) {
            textField.text = [textField.text substringToIndex:textLength];
        }
        self.Value = textField.text;
        
    }else if (textField.tag == TAG_NBR_PER_PERSON) { //每人最多领取多少张
        if (textField.text.length >= textLength) {
            textField.text = [textField.text substringToIndex:textLength];
        }
        
        self.nbrPerPerson = textField.text;
        
    }else if (textField.tag == TAG_LIMITEDNBR) {//每单限用多少张
        if (textField.text.length >=textLength) {
            textField.text = [textField.text substringToIndex:textLength];
        }
        
        self.limitedNbr = textField.text;
        
    }
    
    if (textField.tag == 8000) {
        if (textField.text.length >=textLength) {
            textField.text = [textField.text substringToIndex:textLength];
        }
        
        self.payPrice = textField.text;
    }
    

    
    if (textField.tag ==1) {
        if (textField.text.length >=3) {
            textField.text = [textField.text substringToIndex:3];
        }
        self.nbrPerPerson = textField.text;

    }else if (textField.tag ==2){
        if (textField.text.length >=3) {
            textField.text = [textField.text substringToIndex:3];
        }
        self.limitedNbr = textField.text;

    }else if (textField.tag == 92) {
        if (textField.text.length >=3) {
            textField.text = [textField.text substringToIndex:3];
        }
        self.limitedSendNbr = textField.text;
        
    } else if (textField.tag == 99) {
        if (textField.text.length >=3) {
            textField.text = [textField.text substringToIndex:3];
        }
        self.GetConditions = textField.text;
    }
    else if (textField.tag == TAG_FUNCTION) {
        if (textField.text.length >=25) {
            textField.text = [textField.text substringToIndex:25];
        }
        self.Desctription = textField.text;
        
    }else if (textField.tag == 1001){
        if (textField.text.length >=3) {
            textField.text = [textField.text substringToIndex:3];
        }
        self.Value = textField.text;

    }else if (textField.tag == 1002){  //张数
        if (textField.text.length >=3) {
            textField.text = [textField.text substringToIndex:3];
        }
        self.totalVolume = textField.text;

    }else if (textField.tag == 999){  //满100元 (打九折)
        if (textField.text.length >=3) {
            textField.text = [textField.text substringToIndex:3];
        }
        self.disCount = textField.text;

        
        
    }else if (textField.tag == 7788){  //

//        NSDictionary *attributes = @{NSFontAttributeName: [UIFont systemFontOfSize:13.f]};
//        CGSize contentSize=[chartMessage.content boundingRectWithSize:CGSizeMake(200, MAXFLOAT) options: NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:attributes context:nil].size;
        
        self.remark = textField.text;

    }
    
    
    [self reloadBottomCell];
    
}

- (void)reloadBottomCell{
    
    if (self.type == 1) {
        if (self.Desctription.length>0 && self.Value.length>0&&self.totalVolume.length>0) {
            self.isFindButton = YES;
        }else{
            self.isFindButton = NO;
            
        }
    }
    else if(self.type ==3){
        
        if (self.disCount.length>0 && self.Value.length>0&&self.limitedNbr.length>0) {
            self.isFindButton = YES;
        }else{
            self.isFindButton = NO;
            
        }
        
        
    }
    else if (self.type ==4){
        if ([self.disCount floatValue]>10.0) {
            CSAlert(@"请输入正确的折扣");
            self.disCount = nil;
        }
        if (self.disCount.length>0 && self.Value.length>0&&self.limitedNbr.length>0) {
            self.isFindButton = YES;
        }else{
            self.isFindButton = NO;
            
        }
        
    }
    else if (self.type == 5 ||self.type == 6){
        if (self.Desctription.length >0 && self.totalVolume.length>0) {
            self.isFindButton = YES;
            
        }else{
            self.isFindButton = NO;
            
        }
        
    }
    else if (self.type == 7) {
        
        if (self.Desctription.length >0 && self.disCount.length > 0) {
            self.isFindButton = YES;
            
        }else{
            self.isFindButton = NO;
            
        }
        
        
    }else if (self.type == 8) {
        
        if (self.Desctription.length>0 && self.payPrice.length >0 && self.disCount.length > 0) {
            self.isFindButton = YES;
            
        }else{
            self.isFindButton = NO;
            
        }
        
        
    }
    
    
    
    
    if (self.isSeleSend) {
        if ( self.GetConditions.length>0 && self.limitedSendNbr.length>0 && self.isFindButton) {
            self.isFindButton = YES;
        }else{
            self.isFindButton = NO;
        }
    }
    
    
    NSIndexPath *indexPath=[NSIndexPath indexPathForRow:4 inSection:0];
    [self.myTableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];
}


- (void)seleTop:(UIButton *)button {
    
//    @property (nonatomic, assign)BOOL isTotal; //发行的数量是否限制
//    @property (nonatomic, assign)BOOL isnbrPerPerson;  //是否限制领取/购买的数量
    
    if (button.tag == 1702) { //发行 多少 张
        button.selected = !button.selected;
        self.isTotal = !self.isTotal;
        
    }else if (button.tag == 1704) { // 单人最多领取多少张
        button.selected = !button.selected;
        self.isnbrPerPerson = !self.isnbrPerPerson;
        
    }
    
    
     NSIndexPath *indexPath=[NSIndexPath indexPathForRow:0 inSection:0];
    [self.myTableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];

    
}


-(void)seleData:(UIButton *)button{
    if (button.tag == 100) {  // 使用日期
        button.selected = !button.selected;
        if(button.selected){
            
            NSDate *date = [NSDate date];
            self.startDate = date;
            NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
            [outputFormatter setDateFormat:@"yyyy-MM-dd"];
            NSString *timestamp_str = [outputFormatter stringFromDate:date];
            self.startDateStr = timestamp_str;
            
            NSDate *nextDate = [NSDate dateWithTimeInterval:24*60*60*7 sinceDate:date];
            self.endDate = nextDate;
            timestamp_str = [outputFormatter stringFromDate:nextDate];
            self.endDateStr = timestamp_str;
            
            self.isDateButton = YES;
            
        }else{
            self.isDateButton = NO;
        }
        
    }else if(button.tag == 200){  //使用时间
        button.selected = !button.selected;
        
        if(button.selected){
            self.isTimeButton = YES;
            
            self.startTimeStr = @"09:00";
            self.endTimeStr = @"18:00";

            
        }else{
            self.isTimeButton = NO;
        }
    }
    
    NSIndexPath *indexPath=[NSIndexPath indexPathForRow:1 inSection:0];
    [self.myTableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];
    
    

    
    
}

-(void)seleOtherOne:(UIButton *)button{
//    @property (nonatomic, assign)BOOL isSeleSend;  //选择满就送
//    @property (nonatomic, assign)BOOL isSeleEndTake; //选择领取日期
    if (button.tag == 100) {  //选择领取日期
        button.selected = !button.selected;
        if (button.selected) {
            self.isSeleEndTake = YES;
            self.isSeleSend = NO;
        }else{
            self.isSeleEndTake = NO;
            self.isSeleSend = YES;
        }
       
        
    }else if(button.tag == 200){ //选择满就送
        button.selected = !button.selected;
        if (button.selected) {
            self.isSeleEndTake = NO;
            self.isSeleSend = YES;
        }else{
            self.isSeleEndTake = YES;
            self.isSeleSend = NO;
            
        }
    }
    
    NSIndexPath *indexPath=[NSIndexPath indexPathForRow:2 inSection:0];
    [self.myTableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];

    [self reloadBottomCell];

    
}
-(void)textViewDidBeginEditing:(UITextView *)textView{
    
//    CGRect rect = [textView.superview convertRect:textView.frame toView:self.view];
//    float y = rect.origin.y;

    
    [UIView animateWithDuration:0.2 animations:^{
        self.myTableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-236);
        NSIndexPath *indexPath = [NSIndexPath indexPathForItem:4 inSection:0];
        [self.myTableView  scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
        
        
    } completion:^(BOOL finished) {
        
    }];
 
}
-(void)textViewDidChange:(UITextView *)textView{
    self.remark = textView.text;
}
-(BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
    if ([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        
        [UIView animateWithDuration:0.2 animations:^{
            self.myTableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
            
        } completion:^(BOOL finished) {
            
        }];
        return NO;
    }
    return YES;
}

#pragma mark --比较日期--
-(void)compareDateSuecc{
    
//    NSLog(@"start,ent = %@,%@ %d",self.startDate,self.endDate,(int)[self.startDate compare:self.endDate]);
     int i =[self.startDate compare:self.endDate];
    
    if (i>0) {
        self.endDate = self.startDate;
        self.endDateStr = self.startDateStr;
    }
    
    [self.myTableView reloadData];
}

#pragma mark - 比较使用时间的大小
-(void)compareTimeSuecc{
   
    
    NSMutableString *str1 = [[NSMutableString alloc]initWithString:self.startTimeStr];
    [str1 replaceCharactersInRange:NSMakeRange(2, 1) withString:@""];
    int i = [str1 intValue];
    
    NSMutableString *str2 = [[NSMutableString alloc]initWithString:self.endTimeStr];
    [str2 replaceCharactersInRange:NSMakeRange(2, 1) withString:@""];
    int j = [str2 intValue];
    
    if (j<i) {
        self.endTimeStr = self.startTimeStr;
        CSAlert(@"结束时间不能小于开始时间");
    }
    [self.myTableView reloadData];
    
    
    
}


@end
