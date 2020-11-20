//
//  BMSQ_RecStuViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/14.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_RecStuViewController.h"
#import "ScheduleStr.h"
#import "MobClick.h"
@interface BMSQ_RecStuViewController ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, strong)NSArray *sectionArr;
@property (nonatomic, strong)NSString *classTime;
@property (nonatomic, assign)float classHeight;

//招生简介进入此页面
@property (nonatomic, strong)NSMutableArray *classArray;
@property (nonatomic, strong)NSMutableArray *classArraySH;  //str height


@property (nonatomic, strong)UITableView *ClasstableView;

@property (nonatomic, strong)NSMutableSet *mutablSet;

@property (nonatomic, assign)BOOL sexF;  //yes 男 no 女
@property (nonatomic, strong)NSString *brithdayStr;

@property (nonatomic, strong)UIView *dateView;
@property (nonatomic, strong)UIDatePicker *datePicker;

@end

@implementation BMSQ_RecStuViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"RecStuView"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"RecStuView"];
}


-(void)viewDidLoad{
    
    [super viewDidLoad];
    [self setNavTitle:@"文学堂书画社"];
    [self setNavigationBar];
    [self setNavBackItem];
    float w1 =APP_VIEW_WIDTH/5*2;
    
    
    self.classArray = [[NSMutableArray alloc]init];
    self.classArraySH = [[NSMutableArray alloc]init];
    self.mutablSet = [[NSMutableSet alloc]init];
    self.sexF = YES;
    
    NSDictionary *classTimeDic = [ScheduleStr scheduleStr:self.classTimeDic w:w1-10];
    self.classTime = [classTimeDic objectForKey:@"str"];
    self.classHeight = [[classTimeDic objectForKey:@"height"]floatValue];
    
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(getClassCode:) name:@"getclassCode" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(deleclassCode:) name:@"deleclassCode" object:nil];
    
    UIButton *btn1 = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-60, 20,  APP_VIEW_ORIGIN_Y-20, APP_VIEW_ORIGIN_Y-20)];
    [btn1 setTitle:@"报名" forState:UIControlStateNormal];
    [btn1 setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    btn1.titleLabel.font = [UIFont boldSystemFontOfSize:14];
    [btn1 addTarget:self action:@selector(clickRightButton) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:btn1];
    
    self.sectionArr = @[@"请输入学员姓名",@"请输入学员年级",@"请输入学员所在学校",@"请输入学员出生年月日",@"请输入联系电话"];
    
    self.ClasstableView= [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.ClasstableView.backgroundColor = [UIColor clearColor];
    self.ClasstableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.ClasstableView.delegate = self;
    self.ClasstableView.dataSource = self;
    self.ClasstableView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.ClasstableView];
    
    if (self.isSign) {
        [self listShopClass];
    }
    
    
    
    
    
    
    
    self.dateView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_HEIGHT-200, APP_VIEW_WIDTH, 200)];
    self.dateView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.dateView];
    
    
    UIButton *sumbitBtn = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-60, 0, 50, 20)];
    [sumbitBtn setTitle:@"确定" forState:UIControlStateNormal];
    [sumbitBtn setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
    [self.dateView addSubview:sumbitBtn];
    sumbitBtn.titleLabel.font = [UIFont systemFontOfSize:13.f];
    [sumbitBtn addTarget:self action:@selector(seleDate:) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton *cancelBtn = [[UIButton alloc]initWithFrame:CGRectMake(10, 0, 50, 20)];
    [cancelBtn setTitle:@"取消" forState:UIControlStateNormal];
    [cancelBtn setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
    [self.dateView addSubview:cancelBtn];
    cancelBtn.titleLabel.font = [UIFont systemFontOfSize:13.f];
    [cancelBtn addTarget:self action:@selector(seleDate:) forControlEvents:UIControlEventTouchUpInside];

    sumbitBtn.tag = 888;
    cancelBtn.tag = 999;
    
    self.datePicker = [ [ UIDatePicker alloc] initWithFrame:CGRectMake(0,20,APP_VIEW_WIDTH,180)];
    self.datePicker.backgroundColor = [UIColor whiteColor];
    self.datePicker.datePickerMode = UIDatePickerModeDate;
    [self.dateView addSubview:self.datePicker];
    
    self.dateView.hidden = YES;
    
    
}


-(void)getClassCode:(NSNotification *)noti{
    [self.mutablSet addObject:noti.object];
    
}

-(void)deleclassCode:(NSNotification *)noti{
    [self.mutablSet removeObject:noti.object];
    
}


-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 2;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (section ==0) {
        return self.sectionArr.count+1;
    }else{
        if (self.isSign) {
            return self.classArray.count;
        }else{
            return 1;
            
        }
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==0) {
        return 40;
    }else{
        
        if (self.isSign) {
            NSDictionary *dic = [self.classArraySH objectAtIndex:indexPath.row];
            float heigh = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"height"]]floatValue];
            return heigh+10+50 + 5;  //
        }else{
            return self.classHeight+10+50;
            
        }
    }
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    if (indexPath.section ==0) {
        
        if (indexPath.row == self.sectionArr.count) {
            
            static NSString *ientifier = @"ientifier";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:ientifier];
            if (cell == nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:ientifier];
                cell.backgroundColor = [UIColor clearColor];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH,39)];
                bgView.backgroundColor = [UIColor whiteColor];
                [cell addSubview:bgView];
                UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 50, 39)];
                label.text = @"性别";
                label.textColor = APP_TEXTCOLOR;
                label.font = [UIFont systemFontOfSize:13.f];
                label.textAlignment = NSTextAlignmentCenter;
                [bgView addSubview:label];
                
                UIButton *btn1 = [[UIButton alloc]initWithFrame:CGRectMake(100, 0, 80, 39)];
                [btn1 setTitle:@"    男" forState:UIControlStateNormal];
                btn1.titleLabel.font = [UIFont systemFontOfSize:13.f];
                [btn1 setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
                [btn1 setTitleColor:APP_NAVCOLOR forState:UIControlStateSelected];
                
                [btn1 setImage:[UIImage imageNamed:@"iv_notSelect"] forState:UIControlStateNormal];
                [btn1 setImage:[UIImage imageNamed:@"iv_select"] forState:UIControlStateSelected];
                
                [bgView addSubview:btn1];
                
                
                UIButton *btn2 = [[UIButton alloc]initWithFrame:CGRectMake(180+20, 0, 80, 39)];
                [btn2 setTitle:@"    女" forState:UIControlStateNormal];
                btn2.titleLabel.font = [UIFont systemFontOfSize:13.f];
                [btn2 setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
                [btn2 setTitleColor:APP_NAVCOLOR forState:UIControlStateSelected];
                
                [btn2 setImage:[UIImage imageNamed:@"iv_notSelect"] forState:UIControlStateNormal];
                [btn2 setImage:[UIImage imageNamed:@"iv_select"] forState:UIControlStateSelected];
                
                [bgView addSubview:btn2];
                btn1.tag = 100;
                btn2.tag =200;
                
                
                [btn1 addTarget:self action:@selector(clickSex:) forControlEvents:UIControlEventTouchUpInside];
                [btn2 addTarget:self action:@selector(clickSex:) forControlEvents:UIControlEventTouchUpInside];

            }
            UIButton *button1 = (UIButton *)[cell viewWithTag:100];
            UIButton *button2 = (UIButton *)[cell viewWithTag:200];
            if(self.sexF){
                button1.selected = YES;
                button2.selected = NO;
            }else{
                button1.selected = NO;
                button2.selected = YES;

            }

            return cell;
            
            
        }else{
            
            static NSString *identifier = @"recStrCell";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell==nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.backgroundColor = [UIColor clearColor];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH,39)];
                bgView.backgroundColor = [UIColor whiteColor];
                [cell addSubview:bgView];
                
                UITextField *textField = [[UITextField alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 39)];
                textField.tag = 100;
                textField.backgroundColor = [UIColor whiteColor];
                textField.font = [UIFont systemFontOfSize:13];
                
                [bgView addSubview:textField];
                
                
                UIButton *bgButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 39)];
                bgButton.tag = 200;
                [bgView addSubview:bgButton];
                [bgButton addTarget:self action:@selector(clickBrithDay) forControlEvents:UIControlEventTouchUpInside];
                bgButton.hidden = YES;
                
            }
            
            UITextField *textField = [cell viewWithTag:100];
            textField.placeholder = [self.sectionArr objectAtIndex:indexPath.row];
            UIButton *button = [cell viewWithTag:200];
            if (indexPath.row ==3) {
                button.hidden = NO;
                textField.text = self.brithdayStr;
                
            }else{
                button.hidden = YES;
                textField.text = self.brithdayStr;

                
            }

            
            return cell;
            
        }
        
        
    }else{
        static NSString *identifier = @"classInfoCell";
        ClassInfoCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell==nil) {
            
            cell = [[ClassInfoCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            
            
        }
        if (self.isSign) {
            
            NSDictionary *classDic =[self.classArray objectAtIndex:indexPath.row];
            cell.classCode = [classDic objectForKey:@"classCode"];
            
            NSDictionary *dic = [self.classArraySH objectAtIndex:indexPath.row];
            float heigh = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"height"]]floatValue];
            [cell setClassInfoCell:[self.classArray objectAtIndex:indexPath.row] heigh:heigh+10+50 timeStr:[dic objectForKey:@"str"]];
        }else{
            
            cell.classCode = [self.classDic objectForKey:@"classCode"];
            
            [cell setClassInfoCell:self.classDic heigh:self.classHeight+10+50 timeStr:self.classTime];
        }
        return cell;
    }
    
}
-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 35)];
    bgView.backgroundColor = APP_VIEW_BACKCOLOR;
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 35)];
    [bgView addSubview:label];
    label.font = [UIFont systemFontOfSize:13];
    label.textColor = APP_TEXTCOLOR;
    if (section==0) {
        label.text = @"    学员基础信息";
    }else{
        label.text = @"    报名课程";
        
    }
    
    return bgView;
}
-(CGFloat)tableView:(UITableView *)tableView estimatedHeightForHeaderInSection:(NSInteger)section{
    return 35;
}


#pragma mark --报名--
-(void)clickRightButton{
    
    
    if (self.mutablSet.count==0) {
        
        UIAlertView *alerView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"请选择报名课程" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        [alerView show];
        
        return;
    }
    
    
    
    NSMutableArray *array = [[NSMutableArray alloc]init];
    for (int i=0; i<5; i++) {
        NSIndexPath *indexPath = [NSIndexPath indexPathForRow:i inSection:0];
        UITableViewCell *cell = [self.ClasstableView cellForRowAtIndexPath:indexPath];
        UITextField *textF = (UITextField *)[cell viewWithTag:100];
        if (textF.text.length<=0) {
            
            return;
        }
        [array addObject:textF.text];
        
    }
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    
    NSMutableString *classCodeS = [[NSMutableString alloc]init];
    for (NSString *str in self.mutablSet) {
        [classCodeS appendString:[NSString stringWithFormat:@"%@|",str]];
    }
    [classCodeS deleteCharactersInRange:NSMakeRange(classCodeS.length-1, 1)];
    [params setObject:classCodeS forKey:@"classCode"];
    
    [params setObject:[array objectAtIndex:0] forKey:@"studentName"];
    [params setObject:[array objectAtIndex:1] forKey:@"studentGrade"];
    [params setObject:[array objectAtIndex:2] forKey:@"studentSchool"];
    [params setObject:self.brithdayStr forKey:@"studentBirthday"];
    [params setObject:self.sexF?@"M":@"F" forKey:@"studentSex"];
    
    [params setObject:[array objectAtIndex:4] forKey:@"studentTel"];
    
    [params setObject:self.shopCode forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"signClass" strParams:[gloabFunction getUserCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    [self.jsonPrcClient invokeMethod:@"signClass" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if ([code intValue] ==50000) {
            [SVProgressHUD showSuccessWithStatus:@"报名成功,请等待学校通知"];
        }else{
            [SVProgressHUD showErrorWithStatus:@"报名失败"];
        }
        
        [self.navigationController popViewControllerAnimated:YES];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
    
    
}

-(void)listShopClass{
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.shopCode forKey:@"shopCode"];
    __weak typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"listShopClass" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        
        [weakSelf.classArray addObjectsFromArray:responseObject];
        
        float w1 =APP_VIEW_WIDTH/5*2;
        
        for (int i=0; i<weakSelf.classArray.count; i++) {
            NSDictionary *classTimeDic = [ScheduleStr scheduleStr:[weakSelf.classArray objectAtIndex:i] w:w1-10];
            [weakSelf.classArraySH addObject:classTimeDic];
        }
        
        NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:1];
        [self.ClasstableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
}
-(void)clickSex:(UIButton *)button{
    
    button.selected = YES;
    if (button.tag == 100) {
        self.sexF = YES;
    }else{
        self.sexF = NO;
    }
    
    
     NSIndexPath *indexPath=[NSIndexPath indexPathForRow:self.sectionArr.count inSection:0];
     [self.ClasstableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];
}
-(void)clickBrithDay{
    
    self.dateView .hidden = NO;

}

-(void)seleDate:(UIButton *)button{
    
    
    int i = (int)button.tag;
    if (i == 888) {
        NSDate *date = self.datePicker.date;
        NSDateFormatter *df = [[NSDateFormatter alloc] init];
        [df setDateFormat:@"yyyy-MM-dd"];
        self.brithdayStr = [df stringFromDate:date];
    }
    
    self.dateView .hidden = YES;
    NSIndexPath *indexPath=[NSIndexPath indexPathForRow:3 inSection:0];
    [self.ClasstableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];

    
}

@end

@implementation ClassInfoCell


-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        self.bgView = [[UIView alloc]init];
        self.bgView.backgroundColor = [UIColor whiteColor];
        [self addSubview:self.bgView];
        
        self.line1 = [[UIView alloc]init];
        self.line1.backgroundColor = APP_VIEW_BACKCOLOR;
        [self addSubview:self.line1];
        
        self.line2 = [[UIView alloc]init];
        self.line2.backgroundColor = APP_VIEW_BACKCOLOR;
        [self addSubview:self.line2];
        
        self.line3 = [[UIView alloc]init];
        self.line3.backgroundColor = APP_VIEW_BACKCOLOR;
        [self addSubview:self.line3];
        
        self.className = [[UILabel alloc]init];
        self.className.textAlignment = NSTextAlignmentCenter;
        self.className.numberOfLines = 0;
        self.className.textColor = APP_TEXTCOLOR;
        self.className.font = [UIFont systemFontOfSize:11];
        [self addSubview:self.className];
        
        self.classtime = [[UILabel alloc]init];
        self.classtime.textAlignment = NSTextAlignmentCenter;
        self.classtime.textColor = APP_TEXTCOLOR;
        self.classtime.font = [UIFont systemFontOfSize:11];
        self.classtime.numberOfLines = 0;
        [self addSubview:self.classtime];
        
        self.classTeacher = [[UILabel alloc]init];
        self.classTeacher.textAlignment = NSTextAlignmentCenter;
        self.classTeacher.textColor = APP_TEXTCOLOR;
        self.classTeacher.font = [UIFont systemFontOfSize:11];
        self.classTeacher.numberOfLines = 0;
        [self addSubview:self.classTeacher];
        
        self.classCount = [[UILabel alloc]init];
        self.classCount.textAlignment = NSTextAlignmentCenter;
        self.classCount.textColor = APP_TEXTCOLOR;
        self.classCount.font = [UIFont systemFontOfSize:11];
        self.classCount.numberOfLines = 0;
        [self addSubview:self.classCount];
        
        self.line = [[UILabel alloc]init];
        self.line.backgroundColor = APP_VIEW_BACKCOLOR;
        [self addSubview:self.line];
        
        self.classStr = [[UILabel alloc]init];
        self.classStr.textAlignment = NSTextAlignmentCenter;
        self.classStr.textColor = APP_TEXTCOLOR;
        self.classStr.font = [UIFont systemFontOfSize:11];
        self.classStr.numberOfLines = 0;
        self.classStr.text = @"报名时间";
        [self addSubview:self.classStr];
        
        self.classDate = [[UILabel alloc]init];
        self.classDate.textAlignment = NSTextAlignmentCenter;
        self.classDate.textColor = APP_TEXTCOLOR;
        self.classDate.font = [UIFont systemFontOfSize:11];
        self.classDate.numberOfLines = 0;
        [self addSubview:self.classDate];
        
        self.classFee = [[UILabel alloc]init];
        self.classFee.textAlignment = NSTextAlignmentCenter;
        self.classFee.textColor = APP_TEXTCOLOR;
        self.classFee.font = [UIFont systemFontOfSize:11];
        self.classFee.numberOfLines = 0;
        [self addSubview:self.classFee];
        
        self.recButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 20, 20)];
        self.recButton.layer.cornerRadius = 4;
        self.recButton.layer.masksToBounds = YES;
        self.recButton.layer.borderColor = [APP_TEXTCOLOR CGColor];
        self.recButton.layer.borderWidth = 1;
        [self.recButton setImage:[UIImage imageNamed:@""] forState:UIControlStateNormal];
        [self.recButton setImage:[UIImage imageNamed:@"对号"] forState:UIControlStateSelected];
        [self.recButton addTarget:self action:@selector(clickrecButton:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:self.recButton];
        
        
        
        
    }
    return self;
}
-(void)setClassInfoCell:(NSDictionary *)dic heigh:(float)heigh timeStr:(NSString *)timeStr{
    float w = APP_VIEW_WIDTH/5;
    float w1 =APP_VIEW_WIDTH/5*2;
    
    self.bgView.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, heigh-5);
    
    
    self.line1.frame = CGRectMake(w, 15, 1, heigh-30);
    self.line2.frame = CGRectMake(w+w1, 15, 1, heigh-30);
    self.line3.frame = CGRectMake(w+w+w1, 15, 1, heigh-30);
    
    
    
    self.className.frame = CGRectMake(2, 5, w-4, heigh-50-5);
    self.classtime.frame = CGRectMake(w+2, 5, w1-4, heigh-50-5);
    self.classTeacher.frame = CGRectMake(w+w1+2, 5, w-4, heigh-50-5);
    self.classCount.frame = CGRectMake(w+w+w1+2, 5, w-4, heigh-50-5);
    
    self.className.text = [dic objectForKey:@"className"];
    self.classTeacher.text = [dic objectForKey:@"teacherName"];
    NSString *learnNum = [NSString stringWithFormat:@"%@",[dic objectForKey:@"learnNum"]] ;
    self.classCount.text = [NSString stringWithFormat:@"%d节课",[learnNum intValue]];
    self.classtime.text = timeStr;
    
    self.line.frame = CGRectMake(10, heigh-50, APP_VIEW_WIDTH-20, 1);
    
    self.classStr.frame = CGRectMake(2, 5+self.line.frame.origin.y, w-4, 30);
    self.classDate.frame = CGRectMake(w+2,5+self.line.frame.origin.y, w1-4,30);
    self.classFee.frame =CGRectMake(w+w1+2,5+self.line.frame.origin.y, w-4,30);
    self.recButton.center = CGPointMake(w+w+w1+(w/2), (30)/2+self.line.frame.origin.y+5);
    
    self.classDate.text = [NSString stringWithFormat:@"%@至%@",[dic objectForKey:@"signStartDate"],[dic objectForKey:@"signEndDate"]];
    self.classFee.text = [NSString stringWithFormat:@"%0.2f元",[[dic objectForKey:@"learnFee"]floatValue]];
    
}

-(void)clickrecButton:(UIButton *)button{
    button.selected = !button.selected;
    
    if(button.selected){
        [[NSNotificationCenter defaultCenter]postNotificationName:@"getclassCode" object:self.classCode];
    }else{
        [[NSNotificationCenter defaultCenter]postNotificationName:@"deleclassCode" object:self.classCode];
        
    }
    
    
}



@end
