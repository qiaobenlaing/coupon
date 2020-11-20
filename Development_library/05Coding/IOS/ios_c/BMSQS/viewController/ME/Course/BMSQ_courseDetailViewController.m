//
//  BMSQ_courseDetailViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/29.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_courseDetailViewController.h"

@implementation BMSQ_courseDetailViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavTitle:@"课程详情"];
    
    
    [self getUserClassInfo];
 }

-(void)initClassDetailView:(NSDictionary *)classDic{
    
    
    
    
    NSArray *courseList = [classDic objectForKey:@"courseList"];
    NSMutableString *timeStr = [[NSMutableString alloc]init];
    for (NSDictionary *dic in courseList) {
        [timeStr appendFormat:@"%@ %@-%@\n",[dic objectForKey:@"weekDay"],[dic objectForKey:@"start"],[dic objectForKey:@"end"]];
        

    }
    
    
    UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(25, APP_VIEW_ORIGIN_Y+10, APP_VIEW_WIDTH-50, 50*5)];

    CGSize size = [timeStr boundingRectWithSize:CGSizeMake(bgView.frame.size.width-60, MAXFLOAT)
                                                        options:NSStringDrawingUsesLineFragmentOrigin
                                                     attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13]}
                                                        context:nil].size;
    
    float timeH = size.height >40?size.height:40+10;
    bgView.frame = CGRectMake(25, APP_VIEW_ORIGIN_Y+10, APP_VIEW_WIDTH-50, 50*4+timeH);
    
    bgView.backgroundColor = [UIColor whiteColor];
    bgView.layer.cornerRadius = 5;
    bgView.layer.masksToBounds=YES;
    [self.view addSubview:bgView];
    
    
    NSDictionary *mainCourseInfo = [classDic objectForKey:@"mainCourseInfo"];
    int spx = [[classDic objectForKey:@"weekStep"]intValue];
    NSString *weekStep = spx ==0?@"每天":[NSString stringWithFormat:@"每隔%d周",spx];
    NSString *Date = [NSString stringWithFormat:@"%@至%@",[mainCourseInfo objectForKey:@"courseStartDate"],[mainCourseInfo objectForKey:@"courseEndDate"]];
    
    NSArray *imageArr = @[@"course_name",@"course_time",@"course_address",@"course_spx",@"course_date"];
    NSArray *textArr = @[[mainCourseInfo objectForKey:@"courseName"],timeStr,[mainCourseInfo objectForKey:@"courseAddr"],weekStep,Date];
    
    
    float h = 50;
    for (int i = 0; i<5; i++) {
        
        UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, h, bgView.frame.size.width, 1)];
        lineView.backgroundColor = APP_VIEW_BACKCOLOR;
        [bgView addSubview:lineView];
        
        
        UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 18, 18)];
        [imageView setImage:[UIImage imageNamed:[imageArr objectAtIndex:i]]];
        [bgView addSubview:imageView];
        imageView.center = CGPointMake(20, lineView.frame.origin.y-25);
        float labelY  = lineView.frame.origin.y-45;
        float labelH = 40;
        if (i==1) {
            imageView.center = CGPointMake(20, lineView.frame.origin.y-timeH+25);
            labelY = lineView.frame.origin.y-timeH+10;
            labelH = timeH;

        }
        
        
      
        
        UILabel *messageLabel = [[UILabel alloc]initWithFrame:CGRectMake(52,labelY, bgView.frame.size.width-60, labelH)];
        messageLabel.text = [textArr objectAtIndex:i];
        messageLabel.font = [UIFont systemFontOfSize:13];
        messageLabel.numberOfLines = 0;
        [bgView addSubview:messageLabel];

        if (i == 0) {

            h=h+timeH;

        }else{

            h = h+50;

        }
    }
    
    for (int i=0; i<2; i++) {
        UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(40+(i*5), 0, 0.2, bgView.frame.size.height)];
        lineView.backgroundColor = [UIColor redColor];
        [bgView addSubview:lineView];
    }
    
  
    
    
   
    
    
    UIButton *deleButton = [[UIButton alloc]initWithFrame:CGRectMake((APP_VIEW_WIDTH-150)/2, bgView.frame.origin.y+bgView.frame.size.height+30, 150, 40)];
    [deleButton setTitle:@"删除" forState:UIControlStateNormal];
    [deleButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    deleButton.backgroundColor =UICOLOR(179, 179, 179, 1);
    deleButton.layer.cornerRadius = 5;
    deleButton.layer.masksToBounds = YES;
    [deleButton addTarget:self action:@selector(delUserClassInfo) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:deleButton];
    
}

#pragma mark - 获取课程
-(void)getUserClassInfo{
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.tableCode forKey:@"tableCode"];
    
    NSString* vcode = [gloabFunction getSign:@"getUserClassInfo" strParams:self.tableCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:@""];
    
    [self.jsonPrcClient invokeMethod:@"getUserClassInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        [weakSelf initClassDetailView:responseObject];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
}
#pragma mark - 删除课程
-(void)delUserClassInfo{
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.tableCode forKey:@"tableCode"];
    
    NSString* vcode = [gloabFunction getSign:@"delUserClassInfo" strParams:self.tableCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:@""];
    
    [self.jsonPrcClient invokeMethod:@"delUserClassInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        

        NSDictionary *dic = responseObject;
        
        int code =[[dic objectForKey:@"code"]intValue];
        
        if (code == 50000) {
            [SVProgressHUD showSuccessWithStatus:@"课程删除成功"];
            [weakSelf.navigationController popViewControllerAnimated:YES];
            [[NSNotificationCenter defaultCenter]postNotificationName:@"loadCourse" object:nil];
        }
        else if (code == 20000){
            [SVProgressHUD showErrorWithStatus:@"课程删除失败"];
        }
        else if (code == 77036){
            [SVProgressHUD showErrorWithStatus:@"报名的课程不能删除"];
        }

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        [weakSelf.navigationController popViewControllerAnimated:YES];
    }];
    
}
@end
