//
//  BMSQ_BenefitTableViewCell.m
//  BMSQC
//
//  Created by liuqin on 15/9/12.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_BenefitTableViewCell.h"
#import "UIImageView+WebCache.h"

@interface BMSQ_BenefitTableViewCell ()


@property (nonatomic, strong)UIView *BackView;
@property (nonatomic, strong)UIView *topline;

@property (nonatomic, strong)UIImageView *headImageView;
@property (nonatomic, strong)UILabel *nameLabel;
@property (nonatomic, strong)UILabel *addressLabel;

@property (nonatomic, strong)UILabel *distanceLabe;
@property (nonatomic, strong)UIImageView *popularityImageView;
@property (nonatomic, strong)UILabel *popularityLabel;
@property (nonatomic, strong)UILabel *typeLabel;

@property (nonatomic, strong)UIView *secLine;

@property (nonatomic, strong)UIView *ImageBackView;
@property (nonatomic, strong)UIView *imageLine;

@property (nonatomic, strong)UIView *activityView;
@property (nonatomic, strong)UIImageView *activityImage;
@property (nonatomic, strong)UILabel *ativityLabel;
@property (nonatomic, strong)UILabel *activityLableLabel;
@property (nonatomic, strong)UIView *activityLine;


@property (nonatomic, strong)UIView *typeView;
@property (nonatomic, strong)UIButton *gotoButton;

@property (nonatomic, strong)UIView *bottomline;
@end



@implementation BMSQ_BenefitTableViewCell

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        
        self.backgroundColor = [UIColor colorWithRed:235/255.0 green:233/255.0 blue:241/255.0 alpha:1];
        
        self.BackView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 0)];
        self.BackView.backgroundColor = [UIColor whiteColor];
        [self.contentView addSubview:self.BackView];
        
        
        self.topline = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 0.4)];
        self.topline.backgroundColor = [UIColor grayColor];
        self.topline.alpha= 0.5;
        [self.BackView addSubview:self.topline];
        
        self.headImageView = [[UIImageView alloc]init];
        self.headImageView.backgroundColor = [UIColor clearColor];
        self.headImageView.layer.masksToBounds = YES;
        self.headImageView.layer.cornerRadius = 3;
        [self.BackView addSubview:self.headImageView];
        
        self.nameLabel = [[UILabel alloc]init];
        self.nameLabel.backgroundColor = [UIColor clearColor];
        self.nameLabel.font = [UIFont boldSystemFontOfSize:14.f];
        [self.BackView addSubview:self.nameLabel];
        
        self.addressLabel = [[UILabel alloc]init];
        self.addressLabel.backgroundColor = [UIColor clearColor];
        self.addressLabel.textColor =[UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
        self.addressLabel.font =[UIFont systemFontOfSize:12.f];
        self.addressLabel.numberOfLines = 0;
        [self.BackView addSubview:self.addressLabel];
        
        self.distanceLabe = [[UILabel alloc]init];
        self.distanceLabe.backgroundColor = [UIColor clearColor];
        self.distanceLabe.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
        self.distanceLabe.font = [UIFont systemFontOfSize:11.f];
        self.distanceLabe.textAlignment = NSTextAlignmentRight;
        [self.BackView addSubview:self.distanceLabe];
        
        self.popularityImageView = [[UIImageView alloc]init];
        self.popularityImageView.backgroundColor = [UIColor clearColor];
        [self.popularityImageView setImage:[UIImage imageNamed:@"cup"]];
        
        [self.BackView addSubview:self.popularityImageView];
        
        self.popularityLabel = [[UILabel alloc]init];
        self.popularityLabel.backgroundColor = [UIColor clearColor];
        self.popularityLabel.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
        self.popularityLabel.font = [UIFont systemFontOfSize:11.f];
        
        [self.BackView addSubview:self.popularityLabel];
        
        self.typeLabel = [[UILabel alloc]init];
        self.typeLabel.backgroundColor = [UIColor clearColor];
        self.typeLabel.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
        self.typeLabel.font = [UIFont systemFontOfSize:11.f];
        [self.BackView addSubview:self.typeLabel];

        
        self.secLine = [[UIView alloc]initWithFrame:CGRectMake(0, 60, APP_VIEW_WIDTH, 0.4)];
        self.secLine.backgroundColor = [UIColor grayColor];
        self.secLine.alpha= 0.5;
        [self.BackView addSubview:self.secLine];

        
        self.ImageBackView = [[UIView alloc]init];
        self.ImageBackView.backgroundColor =[ UIColor clearColor];
        [self.BackView addSubview:self.ImageBackView];
        
        self.imageLine = [[UIView alloc]init];
        self.imageLine.backgroundColor = [UIColor grayColor];
        self.imageLine.alpha= 0.5;
        [self.BackView addSubview:self.imageLine];
        
        self.activityView = [[UIView alloc]init];
        self.activityView.backgroundColor = [UIColor clearColor];
        self.activityView.userInteractionEnabled = YES;
        UITapGestureRecognizer *tapGes = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(activityGesture)];
        [self.activityView addGestureRecognizer:tapGes];
        
        [self.BackView addSubview:self.activityView];
        
        
        self.activityLableLabel = [[UILabel alloc]init];
        self.activityLableLabel.font = [UIFont boldSystemFontOfSize:13.f];
        self.activityLableLabel.textAlignment = NSTextAlignmentCenter;
        self.activityLableLabel.layer.masksToBounds = YES;
        self.activityLableLabel.layer.cornerRadius = 3;
        self.activityLableLabel.backgroundColor = [UIColor colorWithRed:228/255.0 green:131/255.0 blue:0 alpha:1];
        self.activityLableLabel.textColor = [UIColor whiteColor];
        self.activityLableLabel.text=@"活动";
        [self.activityView addSubview:self.activityLableLabel];
        
        
        self.ativityLabel = [[UILabel alloc]init];
        self.ativityLabel.font = [UIFont systemFontOfSize:13.f];
        self.ativityLabel.textAlignment = NSTextAlignmentRight;
        [self.activityView addSubview:self.ativityLabel];
        
        
        self.activityImage = [[UIImageView alloc]init];
        self.activityImage.backgroundColor = [UIColor clearColor];
        [self.activityImage setImage:[UIImage imageNamed:@"garyright"]];
        [self.activityView addSubview:self.activityImage];
        
        
        self.activityLine = [[UIView alloc]init];
        self.activityLine.backgroundColor = [UIColor grayColor];
        self.activityLine.alpha= 0.5;
        [self.activityView addSubview:self.activityLine];
        
        self.typeView = [[UIView alloc]init];
        self.typeView.backgroundColor = [UIColor clearColor];
        [self.BackView addSubview:self.typeView];
        
        self.gotoButton = [[UIButton alloc]init];
        [self.gotoButton setTitle:@"进入店铺" forState:UIControlStateNormal];
        self.gotoButton.layer.borderColor = [[UIColor colorWithRed:250/255.0 green:78/255.0 blue:0/255.0 alpha:1]CGColor];
        [self.gotoButton setTitleColor:[UIColor colorWithRed:250/255.0 green:78/255.0 blue:0/255.0 alpha:1] forState:UIControlStateNormal];
        self.gotoButton.layer.cornerRadius = 3;
        self.gotoButton.layer.masksToBounds = YES;
        self.gotoButton.titleLabel.font = [UIFont systemFontOfSize:13.f];
        self.gotoButton.layer.borderWidth = 0.5;
        [self.gotoButton addTarget:self action:@selector(gotoVC) forControlEvents:UIControlEventTouchUpInside];
        [self.BackView addSubview:self.gotoButton];


        
        
        
    }
    return self;
}
-(void)setBenefitCell:(NSDictionary *)dic{
    
    self.detailDic = dic;
    
    NSString *urlStr = [[NSString alloc]initWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"logoUrl"]];
    [self.headImageView sd_setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    self.nameLabel.text = [dic objectForKey:@"shopName"];
    self.addressLabel.text = [dic objectForKey:@"street"];
    
    float distance = [[dic objectForKey:@"distance"]floatValue]/1000;
    
    self.distanceLabe.text =distance>10? [NSString stringWithFormat:@">10km"]:[NSString stringWithFormat:@"%0.2fkm",distance];
    self.popularityLabel.text = [NSString stringWithFormat:@"人气:%@",[dic objectForKey:@"popularity"]];
    
    self.headImageView.frame = CGRectMake(15, 10, APP_VIEW_WIDTH/5, APP_VIEW_WIDTH/5);
    self.nameLabel.frame = CGRectMake(self.headImageView.frame.origin.x+self.headImageView.frame.size.width +10, self.headImageView.frame.origin.y, APP_VIEW_WIDTH-self.headImageView.frame.size.width-15-80, 20) ;
    
    self.distanceLabe.frame = CGRectMake(APP_VIEW_WIDTH-100, 10, 80, 20);
    self.addressLabel.frame =CGRectMake(self.nameLabel.frame.origin.x, 25, APP_VIEW_WIDTH-self.headImageView.frame.origin.x-self.headImageView.frame.size.width-10, self.headImageView.frame.size.height-25)  ;
    
    self.secLine.frame = CGRectMake(0, self.headImageView.frame.origin.y+self.headImageView.frame.size.height+10, APP_VIEW_WIDTH, 0.4);
    
    CGSize size= [self.popularityLabel.text boundingRectWithSize:CGSizeMake(self.distanceLabe.frame.size.width, self.distanceLabe.frame.size.height)
                                                         options:NSStringDrawingUsesLineFragmentOrigin
                                                      attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.f]}
                                                         context:nil].size;
    self.popularityLabel.frame = CGRectMake(APP_VIEW_WIDTH-size.width-10, self.secLine.frame.origin.y-20, size.width, size.height);
    self.popularityImageView.frame = CGRectMake(self.popularityLabel.frame.origin.x-20, self.popularityLabel.frame.origin.y,15, self.popularityLabel.frame.size.height);
    
    int type = [[dic objectForKey:@"type"]intValue];
    switch (type) {
        case 0:
        {
            self.typeLabel.text = @"";
        }
            break;
        case 1:
        {
            self.typeLabel.text = @"美食";
        }
            break;
        case 2:
        {
            self.typeLabel.text = @"丽人";
        }
            break;
        case 3:
        {
            self.typeLabel.text = @"健身";
        }
            break;
        case 4:
        {
            self.typeLabel.text = @"娱乐";
        }
            break;
        case 5:
        {
            self.typeLabel.text = @"服装";
        }
            break;
        case 6:
        {
            self.typeLabel.text = @"其他";
        }
            break;
            
        default:
            break;
    }
    
    self.typeLabel.frame = CGRectMake(10+self.headImageView.frame.origin.x+self.headImageView.frame.size.width, self.secLine.frame.origin.y-20, 30, 10);
    

    
    
    NSArray *newPhotoList = [dic objectForKey:@"newPhotoList"];
    
    for (UIView *views in self.ImageBackView.subviews) {
        [views removeFromSuperview];
    }
    
    
    if (newPhotoList.count >0) {
        self.ImageBackView.frame = CGRectMake(15, self.secLine.frame.origin.y, APP_VIEW_WIDTH-30, APP_VIEW_WIDTH/3) ;
        float w = self.ImageBackView.frame.size.height-20;
        int sp = (self.ImageBackView.frame.size.width-w*3)/2;
        
        for (int i = 0;i<newPhotoList.count;i++) {
            NSDictionary *imageDic = [newPhotoList objectAtIndex:i];
            UIImageView *photoImageView = [[UIImageView alloc]initWithFrame:CGRectMake(i*(w+sp), 10, w, w)];
            NSString *str = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[imageDic objectForKey:@"url"]];
            [photoImageView sd_setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
            [self.ImageBackView addSubview:photoImageView];
            
            UIImageView *samll = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 30, 30)];
            [samll setImage:[UIImage imageNamed:@"smallNew"]];
            [photoImageView addSubview:samll];
            
        }
    }else{
        self.ImageBackView.frame = CGRectMake(15, self.secLine.frame.origin.y, APP_VIEW_WIDTH-30,0) ;
    }
    
    self.imageLine.frame = CGRectMake(0, self.ImageBackView.frame.origin.y+self.ImageBackView.frame.size.height, APP_VIEW_WIDTH, self.ImageBackView.frame.size.height>0? 0.5:0);
    
    
    NSString *activity = [dic objectForKey:@"actName"];
    if (activity.length >0) {
        self.activityView.frame = CGRectMake(0, self.imageLine.frame.origin.y+self.imageLine.frame.size.height, APP_VIEW_WIDTH, 35);
        self.activityLine.frame = CGRectMake(20, 34, APP_VIEW_WIDTH-25, 0.4);
        self.activityImage.frame = CGRectMake(0, 0, 8, 12);
        self.activityImage.center = CGPointMake(APP_VIEW_WIDTH-40, 35/2);
        self.ativityLabel.frame = CGRectMake(20, 0, APP_VIEW_WIDTH-80, 35);
        self.ativityLabel.text = activity;
        self.activityLableLabel.frame = CGRectMake(20, 5, 35, 25);
    
    }else{
         self.activityView.frame = CGRectMake(0, self.imageLine.frame.origin.y+self.imageLine.frame.size.height, APP_VIEW_WIDTH, 0);
        self.activityImage.frame = CGRectMake(0, 0, APP_VIEW_WIDTH-25, 0);
        self.activityLine.frame = CGRectMake(20, 38, APP_VIEW_WIDTH-25, 0);
        self.ativityLabel.frame = CGRectMake(20, 0, APP_VIEW_WIDTH-20-40, 0);
        self.ativityLabel.text = @"";
        self.activityLableLabel.frame = CGRectMake(20, 10, 40, 0);

    }
    
  
    
    NSMutableArray *typeStr = [[NSMutableArray alloc]init];
    NSArray *typeArray = [dic objectForKey:@"couponType"];
    for (id idtype in typeArray) {
        int type = [idtype intValue];
        switch (type) {
            case 0:
            {
//                [typeStr addObject:@"N元购"];
            }
                break;
            case 1:
            {
                [typeStr addObject:@"N元购"];
            }
                break;
            case 2:
            {
//                [typeStr addObject:@"N元购"];

            }
                break;
            case 3:
            {
                [typeStr addObject:@"抵扣券"];
  
            }
                break;
            case 4:
            {
                [typeStr addObject:@"折扣券"];
                
            }
                break;
            case 5:
            {
                [typeStr addObject:@"实物券"];
                
            }
                break;
            case 6:
            {
                [typeStr addObject:@"体验券"];
                
            }
                break;
                
            default:
                break;
        }
        
    }
    
    if(typeStr.count ==0){
        
        [self.typeView removeFromSuperview];
        
    }else{
        int count= typeStr.count>3 ? 3 : (int)typeStr.count;
        
        for (int i=0;i<count;i++){
            UILabel *lable = [[UILabel alloc]initWithFrame:CGRectMake(15+i*45, 0, 40, 25)];
            lable.backgroundColor = [UIColor colorWithRed:249/255.0 green:98/255.0 blue:48/255.0 alpha:1];
            lable.layer.masksToBounds = YES;
            lable.layer.cornerRadius = 3;
            lable.textColor = [UIColor whiteColor];
            lable.text = [typeStr objectAtIndex:i];
            lable.font = [UIFont systemFontOfSize:10.f];
            lable.textAlignment = NSTextAlignmentCenter;
            [self.typeView addSubview:lable];
        }
    }
    
    if ([[NSString stringWithFormat:@"%@",[dic objectForKey:@"hasIcbcDiscount"]] isEqualToString:@"1"]) {
        int count= typeStr.count>3 ? 3 : (int)typeStr.count;

        
        UILabel *lable = [[UILabel alloc]initWithFrame:CGRectMake(15+count*45, 0, 45 , 25)];
        lable.backgroundColor = UICOLOR(182, 0, 12, 1.0);
        lable.layer.masksToBounds = YES;
        lable.layer.cornerRadius = 3;
        lable.textColor = [UIColor whiteColor];
        lable.text = @"工行折扣";
        lable.font = [UIFont systemFontOfSize:10.f];
        lable.textAlignment = NSTextAlignmentCenter;
        [self.typeView addSubview:lable];
    }
    
    
    self.typeView.frame = CGRectMake(0, self.activityView.frame.origin.y+self.activityView.frame.size.height+7, APP_VIEW_WIDTH-80, 25);
    
      self.gotoButton.frame = CGRectMake(APP_VIEW_WIDTH-80, self.activityView.frame.origin.y+self.activityView.frame.size.height+7, 70, 25);
    
    self.BackView.frame = CGRectMake(0, 10, APP_VIEW_WIDTH, self.gotoButton.frame.origin.y+self.gotoButton.frame.size.height+5);
    
    
}

+(CGFloat )cellHeigh:(NSDictionary *)dic{
    
    CGFloat heigh =  APP_VIEW_WIDTH/5 +20;
    
    NSArray *newPhotoList = [dic objectForKey:@"newPhotoList"];
    
    
    if (newPhotoList.count >0) {
   
        heigh = heigh + APP_VIEW_WIDTH/3;
    }
    
    
    NSString *activity = [dic objectForKey:@"actName"];
    if (activity.length >0) {
        
        heigh = heigh + 50;
        
    }
    heigh = heigh + 50;
    
    return heigh;
}

-(void)gotoVC{
    
    [self.beneDelegate gotoDetail:self.detailDic];
    
    
}
-(void)activityGesture{
    [self.beneDelegate gotoActivity:self.detailDic];
}
@end
